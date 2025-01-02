package com.nitron.reign_no_longer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nitron.reign_no_longer.common.block.ReignNoLongerBlocks;
import com.nitron.reign_no_longer.common.border.BorderManagment;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItemGroup;
import com.nitron.reign_no_longer.common.item.custom.functional.OblivionPouchItem;
import com.nitron.reign_no_longer.common.status_effect.custom.EnlightenmentEffect;
import com.nitron.reign_no_longer.common.status_effect.custom.EternalBindEffect;
import com.nitron.reign_no_longer.common.status_effect.custom.SoulMarkEffect;
import com.nitron.reign_no_longer.server.backend.ReignNoLongerKeybindings;
import com.nitron.reign_no_longer.server.events.ReignNoLongerDeathHandler;
import com.nitron.reign_no_longer.server.particles.ReignNoLongerParticles;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.lodestar.lodestone.handlers.ScreenshakeHandler;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.screenshake.ScreenshakeInstance;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static com.nitron.reign_no_longer.server.events.ReignNoLongerDeathHandler.applyKnockbackToNearbyPlayers;




public class ReignNoLonger implements ModInitializer {
	public static final String MOD_ID = "reign_no_longer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final BorderManagment BORDER = new BorderManagment(0, 100, 0, 100);
	public static HashSet<UUID> trappedPlayers = new HashSet<>();
	public static BlockPos positionForTheDie;

	@Override
	public void onInitialize() {
		ReignNoLongerBlocks.init();
		ReignNoLongerItems.init();
		ReignNoLongerItemGroup.init();
		ReignNoLongerKeybindings.init();
		ReignNoLongerParticles.register();
		ReignNoLongerDeathHandler.handleDeath();
		HudRenderCallback.EVENT.register(this::onHudRender);
		registerEffects();
		registerPredicates();
		BORDER.setOn(false);
		trappedPlayers.clear();

		ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "explode"), (server, player, handler, buf, responseSender) -> {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();

			server.execute(() -> {
				World world = player.getWorld();
				if (world instanceof ServerWorld serverWorld) {
					BlockPos pos = BlockPos.ofFloored(new Vec3d(x, y, z));
					Vec3d posV = new Vec3d(x, y, z);
					serverWorld.playSound(null, pos, ReignNoLonger.fates_severance_shatter, SoundCategory.PLAYERS, 10, 1);
					if(player.getWorld().isClient()) ScreenshakeHandler.addScreenshake(new ScreenshakeInstance(50).setIntensity(4f, 0.1f).setEasing(Easing.CUBIC_OUT));
					serverWorld.spawnParticles(ReignNoLonger.SHOCKWAVE, posV.getX(), posV.getY() + 1, posV.getZ(), 1, 0, 0, 0, 0);
					serverWorld.spawnParticles(ReignNoLonger.SHOCKWAVE45, posV.getX(), posV.getY() + 1, posV.getZ(), 1, 0, 0, 0, 0);
					serverWorld.spawnParticles(ReignNoLonger.SHOCKWAVE45I, posV.getX(), posV.getY() + 1, posV.getZ(), 1, 0, 0, 0, 0);
					applyKnockbackToNearbyPlayers(serverWorld, pos);
					serverWorld.spawnParticles(ReignNoLonger.SPARKLE, posV.getX(), posV.getY() + 1, posV.getZ(), 50, 0, 0, 0, 0.1);
					double radius = 1;
					Vec3d blockCenter = Vec3d.ofCenter(pos);
					List<ServerPlayerEntity> nearbyPlayers = serverWorld.getPlayers(player1 ->
							player1.getPos().isInRange(blockCenter, radius)
					);
					for (ServerPlayerEntity player1 : nearbyPlayers) {
						player1.changeGameMode(GameMode.SPECTATOR);
						player1.clearStatusEffects();
					}
				}
			});
		});

		ServerPlayNetworking.registerGlobalReceiver(new Identifier(MOD_ID, "tear"), (server, player, handler, buf, responseSender) -> {
			double x = buf.readDouble();
			double y = buf.readDouble();
			double z = buf.readDouble();

			server.execute(() -> {
				World world = player.getWorld();
				if(world instanceof ServerWorld serverWorld){
					for (ServerPlayerEntity pe : serverWorld.getPlayers()){
						serverWorld.spawnParticles(pe, ReignNoLonger.ERROR, true, x, y, z, 1, 0, 0, 0, 0);
						serverWorld.spawnParticles(pe, ReignNoLonger.BOOM, true, x, y, z, 1, 0, 0, 0, 0);
					}
					serverWorld.playSound(null, BlockPos.ofFloored(new Vec3d(x, y, z)), error_growl, SoundCategory.HOSTILE, 100, 1);
				}
			});
		});

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			BorderManagment border = BORDER;

			for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
				double x = player.getX();
				double y = player.getY();
				double z = player.getZ();

				if(border.isOutside(x, y, z) && !player.getInventory().contains(new ItemStack(ReignNoLongerItems.WRIT_OF_PASSAGE)) && BORDER.isOn && trappedPlayers.contains(player.getUuid())){
					double clampedX = MathHelper.clamp(x, border.minX + 0.5, border.maxX - 0.5);
					double clampedY = MathHelper.clamp(y, border.minY, border.maxY - 2);
					double clampedZ = MathHelper.clamp(z, border.minZ + 0.5, border.maxZ - 0.5);

					player.teleport(clampedX, clampedY, clampedZ);
				}
				if(border.isInside(x, y, z) && BORDER.isOn){
					trappedPlayers.add(player.getUuid());
				}
				if(border.isTooFarOutside(x, y, z) && BORDER.isOn){
					trappedPlayers.remove(player.getUuid());
				}
			}
		});
	}

	//Sounds

	public static final SoundEvent altar_ambience = registerSound("altar_ambience");
	public static final SoundEvent seal_of_confinement = registerSound("seal_of_confinement");
	public static final SoundEvent fates_severance_hit = registerSound("fates_severance_hit");
	public static final SoundEvent fates_severance_shatter = registerSound("fates_severance_shatter");
	public static final SoundEvent implode_charge = registerSound("implode_charge");
	public static final SoundEvent error_growl = registerSound("error_growl");

	private static SoundEvent registerSound(String id) {
		Identifier identifier = Identifier.of(MOD_ID, id);
		return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
	}

	//Effects

	public static final StatusEffect ETERNAL_BIND = new EternalBindEffect();
	public static final StatusEffect SOUL_MARK = new SoulMarkEffect();
	public static final StatusEffect ENLIGHTENMENT = new EnlightenmentEffect();

	public static void registerEffects() {
		Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "eternal_bind"), ETERNAL_BIND);
		Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "soul_mark"), SOUL_MARK);
		Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "enlightenment"), ENLIGHTENMENT);
	}

	private static void registerPredicates(){
		ModelPredicateProviderRegistry.register(ReignNoLongerItems.CONTRACT_STONE, new Identifier("signed"),
				(stack, world, entity, seed) -> {
					if (stack.hasNbt()) {
						if(stack.getOrCreateNbt().getBoolean("signed")){
							return 1.0F;
						}
					}
					return 0.0F;
				}
		);
		ModelPredicateProviderRegistry.register(ReignNoLongerItems.OBLIVION_POUCH, new Identifier("filled"),
				(stack, world, entity, seed) -> {
					if (stack.hasNbt()) {
						if(OblivionPouchItem.getAmountFilled(stack) > 0){
							return 1.0F;
						}
					}
					return 0.0F;
				}
		);
	}

	private void onHudRender(DrawContext context, float tickDelta) {
		MinecraftClient client = MinecraftClient.getInstance();

		// Ensure we're in-game and have a player instance
		if (client.player != null) {
			// Check if the player has the custom status effect
			/*StatusEffectInstance effect = client.player.getStatusEffect(ENLIGHTENMENT); // Replace with your effect reference
			if (effect != null) {
				// Render the overlay
				renderCustomOverlay(context);
			}*/
		}
	}

	public static final Identifier ENLIGHTENMENT_OVERLAY = new Identifier("reign_no_longer", "textures/gui/glitch.png");

	private void renderCustomOverlay(DrawContext context) {
		MinecraftClient client = MinecraftClient.getInstance();
		int screenWidth = client.getWindow().getScaledWidth();
		int screenHeight = client.getWindow().getScaledHeight();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		float alpha = 0.5f;

		RenderSystem.clearColor(1.0f, 1.0f, 1.0f, alpha);

		context.drawTexture(ENLIGHTENMENT_OVERLAY, 0, 0, 0, 0, screenWidth, screenHeight, screenWidth, screenHeight);

		RenderSystem.disableBlend();
	}

	//Registration:
	public static final DefaultParticleType SHOCKWAVE = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "shockwave"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType SHOCKWAVE45 = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "shockwave_45"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType SHOCKWAVE45I = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "shockwave_45i"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType SPARKLE = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "sparkle"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType CONFINE = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "confine"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType IMPLODE = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "implode"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType IMPLODEB = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "implodeb"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType RUNE = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "rune"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType TEAR = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "tear"),
			FabricParticleTypes.simple(true)
	);
	public static final DefaultParticleType ERROR = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "error"),
			FabricParticleTypes.simple(true) // <- Fucking liar, this shit doesnt always spawn
	);
	public static final DefaultParticleType BOOM = Registry.register(Registries.PARTICLE_TYPE,
			new Identifier(MOD_ID, "boom"),
			FabricParticleTypes.simple(true)
	);
}