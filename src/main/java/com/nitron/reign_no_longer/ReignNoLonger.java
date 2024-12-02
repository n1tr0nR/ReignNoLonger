package com.nitron.reign_no_longer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.nitron.reign_no_longer.common.block.ReignNoLongerBlocks;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItemGroup;
import com.nitron.reign_no_longer.common.item.custom.functional.OblivionPouchItem;
import com.nitron.reign_no_longer.common.status_effect.custom.EnlightenmentEffect;
import com.nitron.reign_no_longer.common.status_effect.custom.EternalBindEffect;
import com.nitron.reign_no_longer.common.status_effect.custom.SoulMarkEffect;
import com.nitron.reign_no_longer.server.backend.ReignNoLongerKeybindings;
import com.nitron.reign_no_longer.server.events.PlayerTickManager;
import com.nitron.reign_no_longer.server.particles.ReignNoLongerParticles;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.command.CommandManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReignNoLonger implements ModInitializer {
	public static final String MOD_ID = "reign_no_longer";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		ReignNoLongerBlocks.init();
		ReignNoLongerItems.init();
		ReignNoLongerItemGroup.init();
		ReignNoLongerKeybindings.init();
		ReignNoLongerParticles.register();
		PlayerTickManager.registerEvents();
		HudRenderCallback.EVENT.register(this::onHudRender);
		registerEffects();
		registerPredicates();
	}

	//Sounds

	public static final SoundEvent altar_ambience = registerSound("altar_ambience");
	public static final SoundEvent altar_summon = registerSound("altar_summon");
	public static final SoundEvent altar_break = registerSound("altar_break");
	public static final SoundEvent seal_of_confinement = registerSound("seal_of_confinement");
	public static final SoundEvent fates_severance_hit = registerSound("fates_severance_hit");

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
					if (entity instanceof PlayerEntity player) {
						if(stack.getOrCreateNbt().getBoolean("signed")){
							return 1.0F;
						}
					}
					return 0.0F;
				}
		);
		ModelPredicateProviderRegistry.register(ReignNoLongerItems.OBLIVION_POUCH, new Identifier("filled"),
				(stack, world, entity, seed) -> {
					if (entity instanceof PlayerEntity player) {
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
			StatusEffectInstance effect = client.player.getStatusEffect(ENLIGHTENMENT); // Replace with your effect reference
			if (effect != null) {
				// Render the overlay
				renderCustomOverlay(context);
			}
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
}