package com.nitron.reign_no_longer.common.status_effect.custom;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;

public class EternalBindEffect extends StatusEffect {
    public EternalBindEffect() {
        super(StatusEffectCategory.HARMFUL, 0x4A4A4A);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof ServerPlayerEntity player){
            spawnCylinderParticles(player, 0.6, 2);
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entity instanceof ServerPlayerEntity s){
            s.changeGameMode(GameMode.SPECTATOR);
        }
        super.onRemoved(entity, attributes, amplifier);
    }

    public static void spawnCylinderParticles(ServerPlayerEntity player, double radius, double height) {
        // Get the world of the player
        ServerWorld world = (ServerWorld) player.getWorld();

        // Get the player's position
        double x = player.getX();
        double y = player.getY() + 1;
        double z = player.getZ();

        // Number of particles to spawn (adjust for desired density)
        int particleCount = 5;

        // Spawn particles in a cylinder around the player
        for (int i = 0; i < particleCount; i++) {
            // Calculate angle for the circle
            float angle = (float) (i * (Math.PI * 2) / particleCount);

            // Calculate the X and Z position using polar coordinates
            double particleX = x + radius * MathHelper.cos(angle);
            double particleZ = z + radius * MathHelper.sin(angle);

            double particleY = y + Math.random() * height - height / 2;

            world.spawnParticles(
                    ParticleTypes.END_ROD,
                    particleX, particleY, particleZ,
                    1,
                    0.0, 0.0, 0.0,
                    0
            );
        }
    }
}
