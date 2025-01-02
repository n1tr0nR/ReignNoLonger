package com.nitron.reign_no_longer.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.nitron.reign_no_longer.ReignNoLonger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"), cancellable = true)
    public void reignNoLonger$renderSky(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean bl, Runnable runnable, CallbackInfo ci){
        if(canShowSky()){
            renderCustomSky(camera, tickDelta);
            ci.cancel();
        } else {
        }
    }

    private boolean canShowSky(){
        return client.player != null && client.player.hasStatusEffect(StatusEffects.SPEED);
    }

    private void renderCustomSky(Camera camera, float tickDelta) {
        RenderSystem.clearColor(0.0f, 0.0f, 0.0f, 1.0f); // Black color
        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT, MinecraftClient.IS_SYSTEM_MAC); // Clear color buffer
    }
}
