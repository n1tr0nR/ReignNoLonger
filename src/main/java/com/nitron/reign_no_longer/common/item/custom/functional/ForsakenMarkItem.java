package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.common.item.custom.base.RNLPermaItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ForsakenMarkItem extends RNLPermaItem {
    public ForsakenMarkItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        stack.damage(1, user, (player) -> user.sendToolBreakStatus(user.getActiveHand()));
        return super.use(world, user, hand);
    }
}
