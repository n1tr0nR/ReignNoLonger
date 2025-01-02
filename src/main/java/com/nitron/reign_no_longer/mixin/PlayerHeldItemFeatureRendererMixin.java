package com.nitron.reign_no_longer.mixin;

import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.common.item.custom.functional.FatesSeveranceItem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerHeldItemFeatureRenderer.class)
public abstract class PlayerHeldItemFeatureRendererMixin {
    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    protected void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!entity.isUsingItem()) {
            if ((entity.getMainArm() == Arm.RIGHT && arm == Arm.LEFT) && entity.getMainHandStack().getItem() instanceof FatesSeveranceItem) {ci.cancel();return;}
            if ((entity.getMainArm() == Arm.LEFT && arm == Arm.RIGHT) && entity.getMainHandStack().getItem() instanceof FatesSeveranceItem) {ci.cancel();return;}}
    }
}
