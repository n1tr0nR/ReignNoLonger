package com.nitron.reign_no_longer.server.particles;

import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class OrbitalStrikeParticle extends SpriteBillboardParticle {

	public OrbitalStrikeParticle(ClientWorld world, double x, double y, double z, Sprite sprite) {
		super(world, x, y, z);
		this.sprite = sprite;

		this.maxAge = 40; // Lifetime in ticks (2 seconds)
		this.scale = 0.1f; // Initial scale
		this.setAlpha(1.0f); // Fully opaque at start
	}

	@Override
	public void tick() {
		super.tick();
		if (this.age < this.maxAge) {
			float progress = (float) this.age / this.maxAge;

			// Increase radius
			this.scale = 0.1f + progress * 0.5f;

			// Fade out
			this.setAlpha(1.0f - progress);
		}
	}

	@Override
	public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
		Vec3d cameraPos = camera.getPos();
		float x = (float) (MathHelper.lerp(tickDelta, this.prevPosX, this.x) - cameraPos.x);
		float y = (float) (MathHelper.lerp(tickDelta, this.prevPosY, this.y) - cameraPos.y);
		float z = (float) (MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - cameraPos.z);

		float halfWidth = this.scale;
		float height = 5.0f; // Very tall height

		// Render a low-poly cylinder as multiple quads
		int segments = 16; // Adjust for smoothness
		for (int i = 0; i < segments; i++) {
			float angle1 = (float) (2 * Math.PI * i / segments);
			float angle2 = (float) (2 * Math.PI * (i + 1) / segments);

			float x1 = x + MathHelper.cos(angle1) * halfWidth;
			float z1 = z + MathHelper.sin(angle1) * halfWidth;
			float x2 = x + MathHelper.cos(angle2) * halfWidth;
			float z2 = z + MathHelper.sin(angle2) * halfWidth;

			// Bottom quad
			vertexConsumer.vertex(x1, y, z1).texture(0, 0).next();
			vertexConsumer.vertex(x2, y, z2).texture(1, 0).next();
			vertexConsumer.vertex(x2, y + height, z2).texture(1, 1).next();
			vertexConsumer.vertex(x1, y + height, z1).texture(0, 1).next();
		}
	}

	@Override
	public ParticleTextureSheet getType() {
		return ParticleTextureSheet.PARTICLE_SHEET_LIT;
	}

}
