package com.nitron.reign_no_longer.server.particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class OrbitalStrikeFactory implements ParticleFactory<DefaultParticleType> {
    private final SpriteProvider spriteProvider;

    public OrbitalStrikeFactory(SpriteProvider spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    @Override
    public SpriteBillboardParticle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double dx, double dy, double dz) {
        OrbitalStrikeParticle particle = new OrbitalStrikeParticle(world, x, y, z, this.spriteProvider.getSprite(0, 0));
        return particle;
    }
}
