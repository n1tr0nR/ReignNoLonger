package com.nitron.reign_no_longer.mixin;

import com.nitron.reign_no_longer.ReignNoLonger;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(method = "travel", at = @At("HEAD"), cancellable = true)
    private void freezePlayerMovement(Vec3d movementInput, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.hasStatusEffect(ReignNoLonger.ETERNAL_BIND)) {
            ci.cancel();
        }
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void freezePlayerAttack(Entity target, CallbackInfo ci){
        PlayerEntity player = (PlayerEntity) (Object) this;

        if (player.hasStatusEffect(ReignNoLonger.ETERNAL_BIND)) {
            ci.cancel();
        }
    }
}
