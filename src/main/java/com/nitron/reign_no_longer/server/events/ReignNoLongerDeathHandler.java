package com.nitron.reign_no_longer.server.events;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.ReignNoLongerClient;
import com.nitron.reign_no_longer.common.block.custom.functional.SealOfConfinementBlock;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.common.item.custom.functional.FatesSeveranceItem;
import com.nitron.reign_no_longer.common.item.custom.functional.ForsakenMarkItem;
import com.nitron.reign_no_longer.common.particles.create.RNLShockwave;
import com.nitron.reign_no_longer.common.particles.create.RNLSparkle;
import com.nitron.reign_no_longer.interfaces.Permakilling;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.util.List;


public class ReignNoLongerDeathHandler {
    private static final StatusEffectInstance BIND = new StatusEffectInstance(ReignNoLonger.ETERNAL_BIND, 999999, 0, true, true, true);


    /*
        Handles all the killing, trapping ect. Must return TRUE, or no entity in the game will be able to die.
        - 16/12/2024 - 22:32 - Just changed the entities able to be perma-killed from only the PlayerEntities to LivingEntities
     */
    public static void handleDeath(){
        ServerLivingEntityEvents.ALLOW_DEATH.register((e, d, c) -> {
            if(d.getAttacker() instanceof PlayerEntity p && e instanceof ServerPlayerEntity pb) {
                ItemStack s = S(p);
                if(ReignNoLonger.trappedPlayers.contains(pb.getUuid())){
                    if(ReignNoLonger.positionForTheDie != null){
                        pb.setHealth(pb.getMaxHealth());
                        pb.teleport(ReignNoLonger.positionForTheDie.getX(), ReignNoLonger.positionForTheDie.getY() + 2, ReignNoLonger.positionForTheDie.getZ());
                        pb.addStatusEffect(BIND);
                        pb.getWorld().breakBlock(ReignNoLonger.positionForTheDie, false, p);
                        ReignNoLonger.BORDER.setOn(false);
                        ReignNoLonger.positionForTheDie = null;
                        BlockPos pos = pb.getBlockPos();
                        for (PlayerEntity pe : pb.getWorld().getPlayers()) {
                            pe.sendMessage(Text.of(pb.getEntityName() + "'s body decayed out of reality"));
                        }
                        pb.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,2000, 200, false, false,  false));
                        pb.getServerWorld().spawnParticles(ReignNoLonger.IMPLODE, pos.getX(), pos.getY() + 0.1f, pos.getZ(), 1, 0, 0, 0, 0);
                        pb.getServerWorld().spawnParticles(ReignNoLonger.RUNE, pos.getX(), pos.getY() + 0.1f, pos.getZ(), 1, 0, 0, 0, 0);
                        pb.getServerWorld().spawnParticles(ReignNoLonger.IMPLODEB, pos.getX(), pos.getY(), pos.getZ(), 3, 0, 0, 0, 0);
                        pb.getServerWorld().spawnParticles(ReignNoLonger.TEAR, pos.getX(), pos.getY() + 20, pos.getZ(), 1, 0, 0, 0, 0);
                        return false;
                    }
                }
                if (s != null && s.getItem() instanceof Permakilling a) {
                    boolean trap = a.binding() && !e.hasStatusEffect(ReignNoLonger.ETERNAL_BIND);
                    if (a.breakable()) s.damage(1, p, (player) -> p.sendToolBreakStatus(p.getActiveHand()));
                    return trap ? T(e) : D(e, p, s, H(e, s));
                }
            }
            return true;
        });
    }

    /*
        Checks both items, Main Hand and Offhand, and if they are
        A Perma-killing instance, AND their usable() is the same as the hand they
        are currently in, then it returns that item.
     */
    private static ItemStack S(LivingEntity s){
        if(s instanceof PlayerEntity p) {
            ItemStack m = p.getStackInHand(Hand.MAIN_HAND);
            ItemStack o = p.getStackInHand(Hand.OFF_HAND);
            if (m.getItem() instanceof Permakilling M && M.usable() == Hand.MAIN_HAND) {
                return m;
            } else if (o.getItem() instanceof Permakilling O && O.usable() == Hand.OFF_HAND) {
                return o;
            }
        }
        return null;
    }

    private static Hand H(LivingEntity s, ItemStack stack){
        return (s.getStackInHand(Hand.MAIN_HAND) == stack) ? Hand.OFF_HAND : Hand.MAIN_HAND;
    }
    /*
        Resets the player to full health, but traps them forever, with ETERNAL_BIND
     */
    private static boolean T(LivingEntity p){
        p.setHealth(p.getMaxHealth());
        p.addStatusEffect(BIND);
        if(!(p instanceof PlayerEntity)){
            p.kill();
        }
        return false;
    }


    /*
        Perma-kills the player, setting them to spectator, with no possible way out (Temporary)
     */
    private static boolean D(LivingEntity p, LivingEntity s, ItemStack stack, Hand hand){
        World w = p.getWorld();
        p.setHealth(p.getMaxHealth());
        if(p instanceof ServerPlayerEntity serverPlayer && s instanceof ServerPlayerEntity source){
            for (PlayerEntity pe : w.getPlayers()){
                pe.sendMessage(Text.of(serverPlayer.getEntityName() + " was erased from existence"));
                if(stack.isOf(ReignNoLongerItems.FATES_SEVERANCE)){
                    FatesSeveranceItem.decrement(stack, source, hand);
                } else if(stack.isOf(ReignNoLongerItems.FORSAKEN_MARK)){
                    ForsakenMarkItem.decrement(stack, source, hand);
                }
                Vec3d pos = serverPlayer.getPos();
                BlockPos posB = serverPlayer.getBlockPos();
                serverPlayer.addStatusEffect(BIND);
                serverPlayer.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE,2000, 200, false, false,  false));
                serverPlayer.getServerWorld().spawnParticles(ReignNoLonger.IMPLODE, pos.getX(), pos.getY() + 0.1f, pos.getZ(), 1, 0, 0, 0, 0);
                serverPlayer.getServerWorld().spawnParticles(ReignNoLonger.RUNE, pos.getX(), pos.getY() + 0.1f, pos.getZ(), 1, 0, 0, 0, 0);
                serverPlayer.getServerWorld().spawnParticles(ReignNoLonger.IMPLODEB, pos.getX(), pos.getY(), pos.getZ(), 3, 0, 0, 0, 0);
                serverPlayer.getServerWorld().playSound(null, posB, ReignNoLonger.implode_charge, SoundCategory.PLAYERS, 10, 1);
            }
        } else {
            p.kill();
        }
        return false;
    }

    public static void applyKnockbackToNearbyPlayers(ServerWorld world, BlockPos blockPos) {
        double radius = 10;
        double knockbackStrength = 3;

        Vec3d blockCenter = Vec3d.ofCenter(blockPos);

        List<ServerPlayerEntity> nearbyPlayers = world.getPlayers(player ->
                player.getPos().isInRange(blockCenter, radius)
        );

        for (PlayerEntity player : nearbyPlayers) {
            Vec3d knockbackDirection = player.getPos().subtract(blockCenter).normalize();
            Vec3d knockbackVelocity = knockbackDirection.multiply(knockbackStrength).add(0, 2, 0);

            player.setVelocity(knockbackVelocity);
            player.velocityModified = true;
        }
    }
}



