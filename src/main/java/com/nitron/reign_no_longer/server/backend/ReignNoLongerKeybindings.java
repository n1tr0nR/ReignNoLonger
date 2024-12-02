package com.nitron.reign_no_longer.server.backend;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ReignNoLongerKeybindings {
    public static KeyBinding breakEternalBind;

    public static void init() {
        breakEternalBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.reign_no_longer.break_eternal_bind",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.reign_no_longer.controls"
        ));
    }
}
