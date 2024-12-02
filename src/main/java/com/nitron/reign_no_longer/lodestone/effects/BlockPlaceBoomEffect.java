package com.nitron.reign_no_longer.lodestone.effects;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.registry.common.particle.LodestoneParticleRegistry;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.builder.WorldParticleBuilder;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

import java.awt.*;

public class BlockPlaceBoomEffect {
    public static void spawnBlockPlaceBoomEffect(World level, Vec3d pos) {
        Color startingColor = new Color(255, 160, 0, 255);
        Color endingColor = new Color(255, 50, 50, 0);

        WorldParticleBuilder.create(LodestoneParticleRegistry.SMOKE_PARTICLE)
                .setScaleData(GenericParticleData.create(4f, 0F).setEasing(Easing.SINE_IN_OUT).build())
                .setTransparencyData(GenericParticleData.create(1f, 0.0f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setSpinData(SpinParticleData.create(0.25f, 0.05f).setSpinOffset((level.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                .setLifetime(40)
                .addMotion(0, -0.05, 0)
                .enableNoClip()
                .enableForcedSpawn()
                .spawn(level, pos.x, pos.y, pos.z);
    }
}
