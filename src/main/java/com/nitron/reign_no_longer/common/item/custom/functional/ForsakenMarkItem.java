package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.interfaces.Permakilling;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ForsakenMarkItem extends Item implements Permakilling {
    public ForsakenMarkItem(Settings settings) {
        super(settings);
    }
    private static final String VALUE_KEY = "usages";
    private static final int MAX_VALUE = 6;

    @Override
    public boolean binding() {
        return false;
    }

    @Override
    public Hand usable() {
        return Hand.OFF_HAND;
    }

    @Override
    public boolean breakable() {
        return false;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.getOrCreateNbt().getInt(VALUE_KEY) < MAX_VALUE &&
        stack.getOrCreateNbt().getInt(VALUE_KEY) > 0;
    }

    public static void decrement(ItemStack stack, PlayerEntity player, Hand hand){
        stack.getOrCreateNbt().putInt(VALUE_KEY, stack.getOrCreateNbt().getInt(VALUE_KEY) - 1);
        if(stack.getOrCreateNbt().getInt(VALUE_KEY) <= 0) {
            player.setStackInHand(Hand.OFF_HAND, new ItemStack(Items.AIR));
        }
    }

    private void initializeCustomValue(ItemStack stack) {
        if (!stack.getOrCreateNbt().contains(VALUE_KEY)) {
            stack.getOrCreateNbt().putInt(VALUE_KEY, MAX_VALUE);
        }
    }
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        initializeCustomValue(stack);
        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int value = stack.getOrCreateNbt().getInt(VALUE_KEY);
        return (int) Math.round(13.0F * (float) value / MAX_VALUE);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0xf7d2af;
    }
}
