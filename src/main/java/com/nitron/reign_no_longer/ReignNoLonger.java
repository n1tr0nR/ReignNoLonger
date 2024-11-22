package com.nitron.reign_no_longer;

import com.nitron.reign_no_longer.common.block.ReignNoLongerBlocks;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.common.status_effect.custom.EternalBindEffect;
import com.nitron.reign_no_longer.common.status_effect.custom.SoulMarkEffect;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
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
		registerEffects();
	}

	//Sounds

	public static final SoundEvent altar_ambience = registerSound("altar_ambience");
	public static final SoundEvent altar_summon = registerSound("altar_summon");
	public static final SoundEvent altar_break = registerSound("altar_break");
	public static final SoundEvent retribution = registerSound("retribution");

	private static SoundEvent registerSound(String id) {
		Identifier identifier = Identifier.of(MOD_ID, id);
		return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
	}

	//Effects

	public static final StatusEffect ETERNAL_BIND = new EternalBindEffect();
	public static final StatusEffect SOUL_MARK = new SoulMarkEffect();

	public static void registerEffects() {
		Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "eternal_bind"), ETERNAL_BIND);
		Registry.register(Registries.STATUS_EFFECT, new Identifier(MOD_ID, "soul_mark"), SOUL_MARK);
	}
}