package com.nitron.reign_no_longer.common.block.custom.functional;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.lodestone.effects.BlockPlaceBoomEffect;
import com.nitron.reign_no_longer.server.events.BorderManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.StopSoundS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class SealOfConfinementBlock extends Block {
    private static final ParticleEffect PARTICLE = ParticleTypes.CLOUD;
    private static final int RADIUS = 40; // Adjust radius as needed
    private static final HashSet<UUID> trappedPlayers = new HashSet<>();
    private static final HashMap<BlockPos, Boolean> activeContainments = new HashMap<>();

    public SealOfConfinementBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient && world.getServer() != null) {
            ServerWorld serverWorld = world.getServer().getOverworld(); // Or your desired dimension
            WorldBorder border = serverWorld.getWorldBorder();

            // Configure the border
            border.setCenter(pos.getX() + 0.5, pos.getZ() + 0.5);
            border.setSize(65); // Radius size, adjust as needed
            border.setDamagePerBlock(0.0); // No damage inside
            border.setSafeZone(100000000);
            border.setWarningBlocks(0);
            world.playSound(null, pos, ReignNoLonger.seal_of_confinement, SoundCategory.MUSIC);
            applyKnockbackToNearbyPlayers(serverWorld, pos);
            for(int i = 0; i < 10; i ++){
                BlockPlaceBoomEffect.spawnBlockPlaceBoomEffect(world, new Vec3d(pos.getX() + 0.5F, pos.getY() + 2, pos.getZ() + 0.5F));
            }

            System.out.println("Border placed at: " + pos);
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.literal("Unstable").formatted(Formatting.DARK_GRAY));
        super.appendTooltip(stack, world, tooltip, options);
    }

    private void applyKnockbackToNearbyPlayers(ServerWorld world, BlockPos blockPos) {
        double radius = 10;
        double knockbackStrength = 3;

        Vec3d blockCenter = Vec3d.ofCenter(blockPos);

        List<ServerPlayerEntity> nearbyPlayers = world.getPlayers(player ->
                player.getPos().isInRange(blockCenter, radius)
        );

        for (PlayerEntity player : nearbyPlayers) {
            Vec3d knockbackDirection = player.getPos().subtract(blockCenter).normalize();
            Vec3d knockbackVelocity = knockbackDirection.multiply(knockbackStrength).add(0, 1, 0);

            player.setVelocity(knockbackVelocity);
            player.velocityModified = true;
        }
    }

    private final Set<BlockPos> activeContainmentCenters = new HashSet<>();

    private void startContainment(World world, BlockPos center, int radius) {
        activeContainmentCenters.add(center);
        trappedPlayers.clear();

        for (PlayerEntity player : world.getPlayers()) {
            double distance = player.getPos().distanceTo(Vec3d.of(center));
            if (distance < radius - 0.5) {
                trappedPlayers.add(player.getUuid());
                player.addStatusEffect(new StatusEffectInstance(ReignNoLonger.SOUL_MARK, 999999, 0, false, false, false));
            }
        }

        ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
            if (serverWorld != world) return;

            for (BlockPos activeCenter : new HashSet<>(activeContainmentCenters)) {
                if (!activeContainments.getOrDefault(activeCenter, false)) {
                    activeContainmentCenters.remove(activeCenter);
                    trappedPlayers.clear();
                    continue;
                }

                playAmbientSound(serverWorld, activeCenter);

                spawnParticles(world, activeCenter, radius, world.getTime());

                for (PlayerEntity player : world.getPlayers()) {
                    double distance = player.getPos().distanceTo(Vec3d.of(activeCenter));
                    UUID playerId = player.getUuid();

                    if (trappedPlayers.contains(playerId)) {
                        if (distance >= radius && !player.getInventory().contains(new ItemStack(ReignNoLongerItems.WRIT_OF_PASSAGE))) {
                            player.teleport(activeCenter.getX() + 0.5, activeCenter.getY() + 2, activeCenter.getZ() + 0.5);
                            player.sendMessage(Text.of("§cYou soul was re-summoned, you cannot leave."), true);
                            player.playSound(SoundEvents.ENTITY_LIGHTNING_BOLT_IMPACT, 0.5F, 1F);
                            serverWorld.spawnParticles(ParticleTypes.END_ROD, player.getX(), player.getY() + 0.5F, player.getZ(),
                                    10, 0.1F, 0.5F, 0.1F, 0.1);
                        }
                        if(distance >= radius + 5 && player.getInventory().contains(new ItemStack(ReignNoLongerItems.WRIT_OF_PASSAGE))){
                            trappedPlayers.remove(playerId);
                        }
                    } else {
                        if (distance < radius && !player.getInventory().contains(new ItemStack(ReignNoLongerItems.WRIT_OF_PASSAGE))) {
                            Vec3d pushDirection = player.getPos().subtract(Vec3d.of(activeCenter)).normalize();
                            Vec3d pushTarget = Vec3d.of(activeCenter).add(pushDirection.multiply(radius + 1));
                            player.teleport(pushTarget.x, player.getY(), pushTarget.z);
                        }
                        if(distance < radius - 5 && player.getInventory().contains(new ItemStack(ReignNoLongerItems.WRIT_OF_PASSAGE))){
                            trappedPlayers.add(playerId);
                        }
                    }
                }
            }
        });
    }

    public void stopContainment(World world, BlockPos center) {
        if (activeContainments.containsKey(center)) {
            activeContainments.put(center, false);
            activeContainmentCenters.remove(center);
            for(PlayerEntity player : world.getPlayers()){
                player.clearStatusEffects();
            }
        }
    }

    @Override
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient && world.getServer() != null) {
            ServerWorld serverWorld = world.getServer().getOverworld();
            WorldBorder border = serverWorld.getWorldBorder();

            // Reset the border or remove changes
            border.setCenter(serverWorld.getSpawnPos().getX(), serverWorld.getSpawnPos().getZ());
            border.setSize(60000000.0); // Reset to default Minecraft border size
            stopAllSounds(serverWorld, pos);

            System.out.println("Border removed from: " + pos);
        }
        super.onBreak(world, pos, state, player);
    }



    private void spawnParticles(World world, BlockPos center, int radius, long time) {
        if (world instanceof ServerWorld serverWorld) {
            double rotationSpeed = 0.02;
            double angle = time * rotationSpeed;

            serverWorld.spawnParticles(ParticleTypes.END_ROD, center.getX() + 0.5, center.getY(), center.getZ() + 0.5,
                    1, (double) radius / 2, (double) radius / 2, (double) radius / 2, 0);

            serverWorld.spawnParticles(PARTICLE, center.getX() + 0.5, center.getY(), center.getZ() + 0.5,
                    1, 0, 10, 0, 0);

            for (double theta = 0; theta < Math.PI; theta += Math.PI / 20) {
                for (double phi = 0; phi < 2 * Math.PI; phi += Math.PI / 20) {
                    double x = radius * Math.sin(theta) * Math.cos(phi);
                    double y = radius * Math.cos(theta);
                    double z = radius * Math.sin(theta) * Math.sin(phi);



                    double rotatedX = x * Math.cos(angle) - z * Math.sin(angle);
                    double rotatedZ = x * Math.sin(angle) + z * Math.cos(angle);

                    double worldX = center.getX() + 0.5 + rotatedX;
                    double worldY = center.getY() + y;
                    double worldZ = center.getZ() + 0.5 + rotatedZ;

                    serverWorld.spawnParticles(PARTICLE, worldX, worldY, worldZ, 1, 0, 0, 0, 0.01);
                }
            }
        }
    }

    public VoxelShape makeShape(){
        VoxelShape shape = VoxelShapes.empty();
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.125, 0, 0.125, 0.875, 2, 0.875), BooleanBiFunction.OR);

        return shape;
    }

    private final Map<BlockPos, Long> lastSoundPlayed = new HashMap<>();

    private static final long SOUND_INTERVAL = 400L; // 20 ticks = 1 second

    private void playAmbientSound(World world, BlockPos pos) {
        long currentTick = world.getTime();

        if (lastSoundPlayed.getOrDefault(pos, 0L) + SOUND_INTERVAL <= currentTick) {
            Random random = new Random();
            world.playSound(null, pos, ReignNoLonger.altar_ambience, SoundCategory.AMBIENT, 1.0f, 0.5F + random.nextFloat() * 0.2F);
            lastSoundPlayed.put(pos, currentTick);
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return makeShape();
    }

    private void stopAllSounds(ServerWorld world, BlockPos pos) {
        // Iterate over players in the vicinity
        for (ServerPlayerEntity player : world.getPlayers(player -> player.squaredDistanceTo(Vec3d.of(pos)) < 64 * 64)) {
            // Create and send the stop sound packet
            StopSoundS2CPacket stopPacket = new StopSoundS2CPacket(null, SoundCategory.MUSIC);
            player.networkHandler.sendPacket(stopPacket);
        }
    }

}
