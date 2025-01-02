package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.common.item.custom.ReignNoLongerToolMaterials;
import com.nitron.reign_no_longer.interfaces.Permakilling;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class FatesSeveranceItem extends SwordItem implements Permakilling {
    public FatesSeveranceItem() {
        super(ReignNoLongerToolMaterials.UNHOLY, 5, -2.4F, new FabricItemSettings());
    }
    private static final String VALUE_KEY = "usages";
    private static final int MAX_VALUE = 6;

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Random random = new Random();
        attacker.getWorld().playSound(null, BlockPos.ofFloored(new Vec3d(target.getX(), target.getY(), target.getZ())),
                ReignNoLonger.fates_severance_hit, SoundCategory.PLAYERS, 1F, 0.5F + random.nextFloat() * 0.2F);
        return super.postHit(stack, target, attacker);
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

    public boolean isOneHanded(ItemStack stack) {
        if (stack.getNbt() == null) {
            setOneHanded(stack,false);
        }
        return stack.getNbt().getBoolean("one_handed");
    }

    public void setOneHanded(ItemStack stack, boolean hidden) {
        stack.getOrCreateNbt().putBoolean("one_handed", hidden);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if(otherStack.isEmpty() && clickType == ClickType.RIGHT)
        {
            setOneHanded(stack, !isOneHanded(stack));slot.markDirty();
            player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_COMPARATOR_CLICK, SoundCategory.PLAYERS);
            return true;
        }
        return super.onClicked(stack, otherStack, slot, clickType, player, cursorStackReference);
    }
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return stack.getOrCreateNbt().getInt(VALUE_KEY) < MAX_VALUE &&
                stack.getOrCreateNbt().getInt(VALUE_KEY) > 0;
    }

    public static void decrement(ItemStack stack, PlayerEntity player, Hand hand){
        stack.getOrCreateNbt().putInt(VALUE_KEY, stack.getOrCreateNbt().getInt(VALUE_KEY) - 1);
        if(stack.getOrCreateNbt().getInt(VALUE_KEY) <= 0) {
            player.setStackInHand(hand, new ItemStack(ReignNoLongerItems.SHATTERED_SEVERANCE));
        }
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

    @Override
    public boolean binding() {
        return true;
    }

    @Override
    public Hand usable() {
        return Hand.MAIN_HAND;
    }

    @Override
    public boolean breakable() {
        return false;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if(entity.hasStatusEffect(ReignNoLonger.ETERNAL_BIND)){
            entity.removeStatusEffect(ReignNoLonger.ETERNAL_BIND);
        }
        return super.useOnEntity(stack, user, entity, hand);
    }

    public void onKilled(ServerWorld world, ServerPlayerEntity player, Hand hand) {

    }
}
