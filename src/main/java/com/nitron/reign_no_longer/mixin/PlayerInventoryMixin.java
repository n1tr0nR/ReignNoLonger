package com.nitron.reign_no_longer.mixin;

import com.nitron.reign_no_longer.common.item.custom.functional.OblivionPouchItem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    /*@Inject(method = "setStack", at = @At("HEAD"), cancellable = true)
    private void preventInventoryInteraction(int slot, ItemStack stack, CallbackInfo ci) {
        if (stack.getItem() instanceof OblivionPouchItem) {
            ci.cancel();
        }
    }*/
}
