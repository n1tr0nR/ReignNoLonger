package com.nitron.reign_no_longer;

import com.nitron.reign_no_longer.common.block.ReignNoLongerBlocks;
import com.nitron.reign_no_longer.common.border.BorderManagment;
import com.nitron.reign_no_longer.common.border.BorderRenderer;
import com.nitron.reign_no_longer.common.particles.create.*;
import com.nitron.reign_no_longer.server.particles.OrbitalStrikeFactory;
import com.nitron.reign_no_longer.server.particles.ReignNoLongerParticles;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;

public class ReignNoLongerClient implements ClientModInitializer {

    private static final ManagedShaderEffect DICH_SHADER = ShaderEffectManager.getInstance()
            .manage(new Identifier(ReignNoLonger.MOD_ID, "shaders/post/tetrachromatic.json"));

    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ReignNoLongerBlocks.SEAL_OF_CONFINEMENT, RenderLayer.getCutout());

        ParticleFactoryRegistry.getInstance().register(ReignNoLongerParticles.ORBITAL_STRIKE_PARTICLE, OrbitalStrikeFactory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.SHOCKWAVE, RNLShockwave.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.SHOCKWAVE45, RNLShockwave.Factory1::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.SHOCKWAVE45I, RNLShockwave.Factory2::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.SPARKLE, RNLSparkle.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.CONFINE, RNLConfine.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.IMPLODE, RNLImplode.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.IMPLODEB, RNLImplodeB.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.RUNE, RNLRune.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.TEAR, RNLTear.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.ERROR, RNLError.Factory::new);
        ParticleFactoryRegistry.getInstance().register(ReignNoLonger.BOOM, RNLBoom.Factory::new);

        WorldRenderEvents.BEFORE_DEBUG_RENDER.register((context) -> {
            MatrixStack matrices = context.matrixStack();
            Camera camera = context.camera();

            BorderManagment border = ReignNoLonger.BORDER;
            if(border.isOn) BorderRenderer.render(matrices, camera, border);
        });
    }
}
