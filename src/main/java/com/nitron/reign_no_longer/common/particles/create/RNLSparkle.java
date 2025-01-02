package com.nitron.reign_no_longer.common.particles.create;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class RNLSparkle extends SpriteBillboardParticle {
    public RNLSparkle(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
        this.maxAge = 100;
        this.scale = (random.nextFloat() * 0.5F - 1.0F) * 0.5F;

        this.velocityX = (random.nextDouble() * 2.0 - 1.0) * 0.5;
        this.velocityZ = (random.nextDouble() * 2.0 - 1.0) * 0.5;
        this.velocityY = (random.nextDouble() * 0.5 - 0.25) * 0.2;
        this.collidesWithWorld = false;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.alpha = (-(1/(float)maxAge) * age + 1);
        super.tick();
    }

    @Override
    protected int getBrightness(float tint) {
        return 240;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(DefaultParticleType effect, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            RNLSparkle particle = new RNLSparkle(world, x, y, z);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
