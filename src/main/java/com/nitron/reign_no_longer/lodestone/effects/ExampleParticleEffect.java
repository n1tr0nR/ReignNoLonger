package com.nitron.reign_no_longer.lodestone.effects;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import team.lodestar.lodestone.registry.common.particle.*;
import team.lodestar.lodestone.systems.easing.*;
import team.lodestar.lodestone.systems.particle.builder.*;
import team.lodestar.lodestone.systems.particle.data.*;
import team.lodestar.lodestone.systems.particle.data.color.*;
import team.lodestar.lodestone.systems.particle.data.spin.*;

import java.awt.Color;

public class ExampleParticleEffect implements ServerTickEvents.EndWorldTick{

    public static void spawnExampleParticles(World level, Vec3d pos) {
        Color startingColor = new Color(255, 255, 255, 255);
        Color endingColor = new Color(255, 80, 80, 0);

        WorldParticleBuilder.create(LodestoneParticleRegistry.WISP_PARTICLE)
                .setScaleData(GenericParticleData.create(8f, 0).build())
                .setTransparencyData(GenericParticleData.create(0.75f, 0.0f).build())
                .setColorData(ColorParticleData.create(startingColor, endingColor).setCoefficient(1.4f).setEasing(Easing.BOUNCE_IN_OUT).build())
                .setSpinData(SpinParticleData.create(0.05f, 0.1f).setSpinOffset((level.getTime() * 0.2f) % 6.28f).setEasing(Easing.QUARTIC_IN).build())
                .setLifetime(400)
                .addMotion(0, 5F, 0)
                .enableNoClip()
                .spawn(level, pos.x, pos.y, pos.z);
    }

    @Override
    public void onEndTick(ServerWorld serverWorld) {

    }
}