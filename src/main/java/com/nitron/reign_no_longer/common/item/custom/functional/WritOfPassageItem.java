package com.nitron.reign_no_longer.common.item.custom.functional;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class WritOfPassageItem extends Item {
    public WritOfPassageItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.reign_no_longer.writ_of_passage"));
        super.appendTooltip(stack, world, tooltip, context);
    }
}
