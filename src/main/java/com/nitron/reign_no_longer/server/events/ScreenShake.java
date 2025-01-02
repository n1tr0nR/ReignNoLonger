package com.nitron.reign_no_longer.server.events;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.random.Random;
import org.objectweb.asm.tree.FrameNode;

public class ScreenShake {
    private static final Random random = Random.create();
    private static float shakeIntensity = 0;
    private static int shakeDuration = 0;

    public static void addShake(float intensity, int duration) {
        shakeIntensity = intensity;
        shakeDuration = duration;
    }

    public static void applyShake(MatrixStack matrices) {
        if (shakeDuration > 0) {
            float offsetX = (random.nextFloat() - 0.5f) * shakeIntensity;
            float offsetY = (random.nextFloat() - 0.5f) * shakeIntensity;
            matrices.translate(offsetX, offsetY, 0);
            shakeDuration--;
        }
    }
}
