package com.nitron.reign_no_longer.common.particles.create;

import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import org.joml.Vector3f;

public class RNLConfine extends SpriteBillboardParticle {
    public RNLConfine(ClientWorld clientWorld, double d, double e, double f) {
        super(clientWorld, d, e, f);
        this.maxAge = 50;
        this.scale = 0.05f;
        this.gravityStrength = 0.05f;
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
        this.alpha = this.alpha * (-(1/(float)maxAge) * age + 1);
        this.alpha = this.alpha * (-(1/(float)maxAge) * age + 1);
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
            RNLConfine particle = new RNLConfine(world, x, y, z);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
