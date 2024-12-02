package com.nitron.reign_no_longer;

import com.nitron.reign_no_longer.common.block.ReignNoLongerBlocks;
import com.nitron.reign_no_longer.server.particles.OrbitalStrikeFactory;
import com.nitron.reign_no_longer.server.particles.ReignNoLongerParticles;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.RenderLayer;

public class ReignNoLongerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ReignNoLongerBlocks.SEAL_OF_CONFINEMENT, RenderLayer.getCutout());

        ParticleFactoryRegistry.getInstance().register(ReignNoLongerParticles.ORBITAL_STRIKE_PARTICLE, OrbitalStrikeFactory::new);
    }
}
