package com.nitron.reign_no_longer;

import com.nitron.reign_no_longer.common.block.ReignNoLongerBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.impl.client.rendering.BlockEntityRendererRegistryImpl;
import net.minecraft.client.render.RenderLayer;

public class ReignNoLongerClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlock(ReignNoLongerBlocks.SOUL_BOUND_ALTAR, RenderLayer.getCutout());
    }
}
