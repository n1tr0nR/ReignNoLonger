package com.nitron.reign_no_longer.common.particles.create;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;


@Environment(EnvType.CLIENT)
public class RNLShockwave extends SpriteBillboardParticle {
    private final Quaternionf randomRotation;
    private final float rotSpeed;

    public RNLShockwave(ClientWorld world, double x, double y, double z, Quaternionf customRotation, float initialRotationSpeed) {
        super(world, x, y, z);
        this.scale = 0.1F;
        this.maxAge = 125;
        this.randomRotation = customRotation;
        this.rotSpeed = initialRotationSpeed;
    }

    @Override
    public boolean shouldCull() {
        return false;
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        Vec3d cameraPos = camera.getPos();
        float f = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.getX());
        float g = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.getY());
        float h = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.getZ());
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        RenderSystem.depthMask(false);
        float rotationAngle = (this.age + tickDelta) * this.rotSpeed;
        Quaternionf dynamicRotation;
        dynamicRotation = new Quaternionf().rotationX(rotationAngle);
        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };
        float particleSize = this.getSize(tickDelta);
        dynamicRotation.mul(this.randomRotation);
        for (int i = 0; i < 4; i++) {
            Vector3f vertex = vertices[i];
            vertex.rotate(dynamicRotation);
            vertex.mul(particleSize);
            vertex.add(f, g, h);
        }
        int brightness = this.getBrightness(tickDelta);
        this.vertex(vertexConsumer, vertices[0], this.getMaxU(), this.getMaxV(), brightness);
        this.vertex(vertexConsumer, vertices[1], this.getMaxU(), this.getMinV(), brightness);
        this.vertex(vertexConsumer, vertices[2], this.getMinU(), this.getMinV(), brightness);
        this.vertex(vertexConsumer, vertices[3], this.getMinU(), this.getMaxV(), brightness);
        this.vertex(vertexConsumer, vertices[0], this.getMaxU(), this.getMaxV(), brightness);
        this.vertex(vertexConsumer, vertices[3], this.getMinU(), this.getMaxV(), brightness);
        this.vertex(vertexConsumer, vertices[2], this.getMinU(), this.getMinV(), brightness);
        this.vertex(vertexConsumer, vertices[1], this.getMaxU(), this.getMinV(), brightness);
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
    }

    private void vertex(VertexConsumer vertexConsumer, Vector3f position, float u, float v, int light) {
        vertexConsumer.vertex(position.x(), position.y(), position.z())
                .texture(u, v)
                .color(this.red, this.green, this.blue, this.alpha)
                .light(light)
                .next();
    }

    @Override
    public int getBrightness(float tint) {
        return 240;
    }

    @Override
    public void tick() {
        super.tick();
        this.scale += 0.7f;
        this.alpha = (-(1/(float)maxAge) * age + 1);
        if (this.age >= this.maxAge) {
            this.markDead();
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleFactory<DefaultParticleType>{
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        Quaternionf upRotation = new Quaternionf().rotationX((float) Math.toRadians(90));

        @Override
        public Particle createParticle(DefaultParticleType effect, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            RNLShockwave particle = new RNLShockwave(world, x, y, z, upRotation, 0);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }

    public static class Factory1 implements ParticleFactory<DefaultParticleType>{
        private final SpriteProvider spriteProvider;

        public Factory1(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        Quaternionf rotation45 = new Quaternionf().rotationX((float) Math.toRadians(90 + 25));

        @Override
        public Particle createParticle(DefaultParticleType effect, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            RNLShockwave particle = new RNLShockwave(world, x, y, z, rotation45, -0.01F);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }

    public static class Factory2 implements ParticleFactory<DefaultParticleType>{
        private final SpriteProvider spriteProvider;

        public Factory2(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }
        Quaternionf rotationNegative45 = new Quaternionf().rotationX((float) Math.toRadians(90 - 25));

        @Override
        public Particle createParticle(DefaultParticleType effect, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            RNLShockwave particle = new RNLShockwave(world, x, y, z, rotationNegative45, 0.01F);
            particle.setSprite(this.spriteProvider);
            return particle;
        }
    }
}
