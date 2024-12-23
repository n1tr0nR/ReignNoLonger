package com.nitron.reign_no_longer.mixin;

import com.nitron.reign_no_longer.ReignNoLonger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {
    @Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    private void blockMouseIfEffectActive(long window, int button, int action, int mods, CallbackInfo info) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && client.player.isAlive() && client.player.hasStatusEffect(ReignNoLonger.ETERNAL_BIND)&& !MinecraftClient.getInstance().isPaused()) {
            KeyBinding.unpressAll();
            info.cancel();
        }
    }
}
