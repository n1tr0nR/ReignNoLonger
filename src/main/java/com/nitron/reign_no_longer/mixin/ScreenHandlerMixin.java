package com.nitron.reign_no_longer.mixin;

import com.nitron.reign_no_longer.common.item.custom.functional.OblivionPouchItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ScreenHandler.class)
public abstract class ScreenHandlerMixin {
    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    private void reignNoLonger$stopShiftClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (slotIndex >= 0 && actionType == SlotActionType.QUICK_MOVE) {
            Slot slot = ((ScreenHandler) (Object) this).slots.get(slotIndex);
            if (slot != null && slot.hasStack()) {
                ItemStack stackInSlot = slot.getStack();
                if (stackInSlot.getItem() instanceof OblivionPouchItem) {
                    if (slot.inventory instanceof PlayerInventory) {
                        ci.cancel();
                    }
                }
            }
        }
    }

    @Inject(method = "onSlotClick", at = @At("HEAD"), cancellable = true)
    private void reignNoLonger$stopDropping(int slotIndex, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (slotIndex >= 0 && actionType != SlotActionType.QUICK_MOVE) {
            Slot slot = ((ScreenHandler) (Object) this).slots.get(slotIndex);
            if (slot != null) {
                ItemStack stackInHand = player.currentScreenHandler.getCursorStack();
                if (!stackInHand.isEmpty() && stackInHand.getItem() instanceof OblivionPouchItem) {
                    if (!(slot.inventory instanceof PlayerInventory)) {
                        ci.cancel();
                    }
                }
            }
        }
    }
}



