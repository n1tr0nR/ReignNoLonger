package com.nitron.reign_no_longer.common.border;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

public class BorderRenderer {
    public static void render(MatrixStack matrices, Camera camera, BorderManagment cubeBorder) {
        Box box = new Box(cubeBorder.minX, cubeBorder.minY, cubeBorder.minZ, cubeBorder.maxX, cubeBorder.maxY, cubeBorder.maxZ);
        VertexConsumerProvider.Immediate vertexConsumers = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        VertexConsumer lines = vertexConsumers.getBuffer(RenderLayer.getLines());
        matrices.push();
        matrices.translate(-camera.getPos().x, -camera.getPos().y, -camera.getPos().z);
        RenderUtils.drawCheckerPattern(matrices, lines,
                box.minX, box.minY, box.minZ,
                box.maxX, box.maxY, box.maxZ,
                2,
                (float) 255 / 255, (float) 199 / 255, (float) 110 / 255, 0.5f
        );
        vertexConsumers.draw();
        matrices.pop();
    }
}
