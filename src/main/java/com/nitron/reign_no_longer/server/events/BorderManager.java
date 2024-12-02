package com.nitron.reign_no_longer.server.events;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;

import java.util.HashMap;

public class BorderManager {
    private static final HashMap<BlockPos, WorldBorder> customBorders = new HashMap<>();

    /**
     * Creates a custom WorldBorder centered at the given position with the specified radius.
     *
     * @param world  The server world where the border is created.
     * @param center The center of the border (block position).
     * @param radius The radius of the border (in blocks).
     */
    public static void createWorldBorder(ServerWorld world, BlockPos center, int radius) {
        WorldBorder border = world.getWorldBorder();
        border.setCenter(center.getX(), center.getZ());
        border.setSize(20.0); // Set a reasonable size
        border.setWarningBlocks(5);

        // Store the border in the map
        customBorders.put(center, border);
    }

    /**
     * Removes a custom WorldBorder at the given position.
     *
     * @param center The center of the border to remove.
     */
    public static void removeWorldBorder(BlockPos center) {
        customBorders.remove(center);
    }

    /**
     * Checks if a given position is inside the custom border at a specific center.
     *
     * @param pos    The position to check.
     * @param center The center of the border.
     * @return True if inside, false otherwise.
     */
    public static boolean isInsideBorder(BlockPos pos, BlockPos center) {
        WorldBorder border = customBorders.get(center);
        return border != null && border.contains(pos.getX(), pos.getZ());
    }

    /**
     * Gets all custom WorldBorders (for rendering or other logic).
     *
     * @return A HashMap of all custom borders.
     */
    public static HashMap<BlockPos, WorldBorder> getCustomBorders() {
        return customBorders;
    }
}

