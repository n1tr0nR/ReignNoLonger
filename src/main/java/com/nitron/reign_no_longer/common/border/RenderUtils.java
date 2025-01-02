package com.nitron.reign_no_longer.common.border;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import software.bernie.geckolib.core.object.Axis;

public class RenderUtils {
    static void drawCheckerPattern(MatrixStack matrices, VertexConsumer lines,
                                   double minX, double minY, double minZ,
                                   double maxX, double maxY, double maxZ,
                                   double squareSize, float red, float green, float blue, float alpha) {
        boolean fill = true;
        for (double x = minX; x < maxX; x += squareSize) {
            for (double y = minY; y < maxY; y += squareSize) {
                if (fill) drawSquare(matrices, lines, x, y, minZ, squareSize, 0, red, green, blue, alpha);
                fill = !fill;
            }
            fill = !fill;
        }
        for (double x = minX; x < maxX; x += squareSize) {
            for (double y = minY; y < maxY; y += squareSize) {
                if (fill) drawSquare(matrices, lines, x, y, maxZ, squareSize, 0, red, green, blue, alpha);
                fill = !fill;
            }
            fill = !fill;
        }
        for (double z = minZ; z < maxZ; z += squareSize) {
            for (double y = minY; y < maxY; y += squareSize) {
                if (fill) drawSquare(matrices, lines, minX, y, z, 0, squareSize, red, green, blue, alpha);
                fill = !fill;
            }
            fill = !fill;
        }
        for (double z = minZ; z < maxZ; z += squareSize) {
            for (double y = minY; y < maxY; y += squareSize) {
                if (fill) drawSquare(matrices, lines, maxX, y, z, 0, squareSize, red, green, blue, alpha);
                fill = !fill;
            }
            fill = !fill;
        }
        for (double x = minX; x < maxX; x += squareSize) {
            for (double z = minZ; z < maxZ; z += squareSize) {
                if (fill) drawSquare(matrices, lines, x, minY, z, squareSize, 0, red, green, blue, alpha);
                fill = !fill;
            }
            fill = !fill;
        }
        for (double x = minX; x < maxX; x += squareSize) {
            for (double z = minZ; z < maxZ; z += squareSize) {
                if (fill) drawSquare(matrices, lines, x, maxY, z, squareSize, 0, red, green, blue, alpha);
                fill = !fill;
            }
            fill = !fill;
        }
    }

    private static void drawSquare(MatrixStack matrices, VertexConsumer lines,
                                   double x, double y, double z,
                                   double width, double height,
                                   float red, float green, float blue, float alpha) {
        double offset = 0.01;
        WorldRenderer.drawBox(
                matrices,
                lines,
                x + offset, y + offset, z + offset,
                x + width - offset, y + height - offset, z + offset,
                red, green, blue, alpha
        );
    }

    private static void drawFace(MatrixStack matrices, VertexConsumer lines,
                                 double minX, double minY, double minZ,
                                 double maxX, double maxY, double maxZ,
                                 double squareSize, float red, float green, float blue, float alpha) {
        boolean fill = true;
        double stepX = Math.abs(maxX - minX) > 0 ? squareSize : 0;
        double stepY = Math.abs(maxY - minY) > 0 ? squareSize : 0;
        double stepZ = Math.abs(maxZ - minZ) > 0 ? squareSize : 0;

        for (double x = minX; x < maxX || (stepX == 0 && x == minX); x += stepX > 0 ? stepX : 1) {
            for (double y = minY; y < maxY || (stepY == 0 && y == minY); y += stepY > 0 ? stepY : 1) {
                for (double z = minZ; z < maxZ || (stepZ == 0 && z == minZ); z += stepZ > 0 ? stepZ : 1) {
                    if (fill) {
                        double rectWidth = stepX > 0 ? squareSize : 0;
                        double rectHeight = stepY > 0 ? squareSize : 0;
                        double rectDepth = stepZ > 0 ? squareSize : 0;
                        WorldRenderer.drawBox(
                                matrices,
                                lines,
                                x,
                                y,
                                z,
                                x + rectWidth,
                                y + rectHeight,
                                z + rectDepth,
                                red,
                                green,
                                blue,
                                alpha
                        );
                    }
                    fill = !fill;
                }
                fill = !fill;
            }
        }
    }
}
