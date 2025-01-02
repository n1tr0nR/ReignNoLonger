package com.nitron.reign_no_longer.common.status_effect.custom;

import com.nitron.reign_no_longer.ReignNoLonger;
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

    public static void spawnCylinderParticles(ServerPlayerEntity player, double radius, double height) {
        ServerWorld world = (ServerWorld) player.getWorld();

        double x = player.getX();
        double y = player.getY() + 1;
        double z = player.getZ();

        int particleCount = 10;

        for (int i = 0; i < particleCount; i++) {
            float angle = (float) (i * (Math.PI * 2) / particleCount);

            double particleX = x + radius * MathHelper.cos(angle);
            double particleZ = z + radius * MathHelper.sin(angle);
            double particleY = y + Math.random() * height - height / 2;

            world.spawnParticles(
                    ReignNoLonger.CONFINE,
                    particleX, particleY, particleZ,
                    1,
                    0.0, 0.0, 0.0,
                    0
            );
        }
    }
}
