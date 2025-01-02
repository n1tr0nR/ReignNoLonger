package com.nitron.reign_no_longer.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.effect.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void modifyFogColor(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci){
        if (shouldUseCustomFog()) {
            RenderSystem.setShaderFogColor(0.0f, 0.0f, 0.0f);
        }
    }
    private static boolean shouldUseCustomFog() {
        MinecraftClient client = MinecraftClient.getInstance();
        return client.player != null && client.player.hasStatusEffect(StatusEffects.SPEED);
    }
}
