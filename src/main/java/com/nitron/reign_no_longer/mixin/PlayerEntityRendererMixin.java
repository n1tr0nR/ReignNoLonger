package com.nitron.reign_no_longer.mixin;

import com.nitron.reign_no_longer.common.item.custom.functional.FatesSeveranceItem;
import com.nitron.reign_no_longer.common.item.custom.functional.ShatteredSeveranceItem;
import com.nitron.reign_no_longer.common.item.custom.functional.VoidborneWeapons;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Debug(export = true)
@Mixin(value = PlayerEntityRenderer.class, priority = 1001)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {


    public PlayerEntityRendererMixin(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "getArmPose", at = @At("HEAD"), cancellable = true)
    private static void reignNoLonger$getArmPoseDR(AbstractClientPlayerEntity player, Hand hand, CallbackInfoReturnable<BipedEntityModel.ArmPose> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.getItem() instanceof FatesSeveranceItem ||
        itemStack.getItem() instanceof ShatteredSeveranceItem) {
            if (!player.isUsingItem() && !itemStack.getOrCreateNbt().getBoolean("one_handed")) {
                cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_CHARGE);
            }
        } else if(itemStack.getItem() instanceof VoidborneWeapons){
            if(!player.isUsingItem()){
                cir.setReturnValue(BipedEntityModel.ArmPose.CROSSBOW_CHARGE);
            }
        }
    }
}