package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.ReignNoLonger;
import net.minecraft.client.item.BundleTooltipData;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class OblivionPouchItem extends Item {
    private static final String ITEMS_KEY = "Items";
    public static final int MAX_STORAGE = 999999999;
    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4F, 0.4F, 1.0F);

    public OblivionPouchItem(Settings settings) {
        super(settings);
    }

    // Returns how full the briefcase is, as a percentage (0.0 to 1.0)
    public static float getAmountFilled(ItemStack stack) {
        return (float) getBundleOccupancy(stack) / MAX_STORAGE;
    }

    @Override
    public boolean onStackClicked(ItemStack oblivionPouchItem, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        }

        ItemStack slotStack = slot.getStack();
        if (slotStack.isEmpty()) {
            this.playRemoveOneSound(player);
            removeFirstStack(oblivionPouchItem).ifPresent(removedStack -> slot.insertStack(removedStack));
        } else if (slotStack.getItem().canBeNested()) {
            int transferred = addToBundle(oblivionPouchItem, slotStack);
            if (transferred > 0) {
                this.playInsertSound(player);
            }
        }
        return true;
    }

    @Override
    public boolean onClicked(ItemStack oblivionPouchItem, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            if (otherStack.isEmpty()) {
                removeFirstStack(oblivionPouchItem).ifPresent(stack -> {
                    this.playRemoveOneSound(player);
                    cursorStackReference.set(stack);
                });
            } else {
                int transferred = addToBundle(oblivionPouchItem, otherStack);
                if (transferred > 0) {
                    this.playInsertSound(player);
                    otherStack.decrement(transferred);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack briefcaseStack = user.getStackInHand(hand);
        if (dropAllBundledItems(briefcaseStack, user)) {
            this.playDropContentsSound(user);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(briefcaseStack, world.isClient());
        }
        return TypedActionResult.fail(briefcaseStack);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("item.reign_no_longer.oblivion_pouch.fullness", getBundleOccupancy(stack), MAX_STORAGE).formatted(Formatting.GRAY));
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        DefaultedList<ItemStack> storedItems = DefaultedList.of();
        getBundledStacks(stack).forEach(storedItems::add);
        return Optional.of(new BundleTooltipData(storedItems, getBundleOccupancy(stack)));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return getBundleOccupancy(stack) > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.min(1 + 12 * getBundleOccupancy(stack) / MAX_STORAGE, 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    private static int getBundleOccupancy(ItemStack stack) {
        return getBundledStacks(stack)
                .mapToInt(s -> getItemOccupancy(s) * s.getCount())
                .sum();
    }

    private static int getItemOccupancy(ItemStack stack) {
        return 64 / stack.getMaxCount();
    }

    private static int addToBundle(ItemStack briefcaseStack, ItemStack stackToAdd) {
        if (stackToAdd.isEmpty() || !stackToAdd.getItem().canBeNested()) {
            return 0;
        }

        NbtCompound nbt = briefcaseStack.getOrCreateNbt();
        NbtList items = nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);

        int currentOccupancy = getBundleOccupancy(briefcaseStack);
        int itemOccupancy = getItemOccupancy(stackToAdd);
        int remainingCapacity = MAX_STORAGE - currentOccupancy;

        // Calculate transferable amount
        int transferableAmount = Math.min(stackToAdd.getCount(), remainingCapacity / itemOccupancy);
        if (transferableAmount <= 0) {
            return 0; // No room in the briefcase
        }

        boolean merged = false;
        for (int i = 0; i < items.size(); i++) {
            NbtCompound existingNbt = items.getCompound(i);
            ItemStack existingStack = ItemStack.fromNbt(existingNbt);

            // Check if the stacks can be merged
            if (ItemStack.canCombine(existingStack, stackToAdd)) {
                int maxAddable = Math.min(transferableAmount, existingStack.getMaxCount() - existingStack.getCount());
                if (maxAddable > 0) {
                    existingStack.increment(maxAddable);
                    existingStack.writeNbt(existingNbt); // Update existing NBT entry
                    transferableAmount -= maxAddable;
                    merged = true;
                    break; // Stop after merging
                }
            }
        }

        // If not merged, create a new entry
        if (!merged && transferableAmount > 0) {
            ItemStack newStack = stackToAdd.copyWithCount(transferableAmount);
            NbtCompound newNbt = new NbtCompound();
            newStack.writeNbt(newNbt);
            items.add(newNbt);
        }

        // Save updated NBT list
        nbt.put(ITEMS_KEY, items);

        // Decrement the source stack in the player's inventory
        stackToAdd.decrement(transferableAmount);
        return transferableAmount;
    }

    private static Optional<NbtCompound> findMergeableEntry(ItemStack stack, NbtList items) {
        return items.stream()
                .filter(NbtCompound.class::isInstance)
                .map(NbtCompound.class::cast)
                .filter(nbt -> ItemStack.canCombine(ItemStack.fromNbt(nbt), stack))
                .findFirst();
    }

    private static Optional<ItemStack> removeFirstStack(ItemStack briefcaseStack) {
        NbtCompound nbt = briefcaseStack.getNbt();
        if (nbt == null || !nbt.contains(ITEMS_KEY)) {
            return Optional.empty();
        }

        NbtList items = nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        if (items.isEmpty()) {
            return Optional.empty();
        }

        NbtCompound firstItemNbt = items.getCompound(0);
        ItemStack firstStack = ItemStack.fromNbt(firstItemNbt);
        items.remove(0);

        if (items.isEmpty()) {
            briefcaseStack.removeSubNbt(ITEMS_KEY);
        }

        return Optional.of(firstStack);
    }

    private static boolean dropAllBundledItems(ItemStack briefcaseStack, PlayerEntity player) {
        NbtCompound nbt = briefcaseStack.getNbt();
        if (nbt == null || !nbt.contains(ITEMS_KEY)) {
            return false;
        }

        if (player instanceof ServerPlayerEntity) {
            NbtList items = nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < items.size(); i++) {
                ItemStack itemStack = ItemStack.fromNbt(items.getCompound(i));
                player.dropItem(itemStack, true);
            }
        }

        briefcaseStack.removeSubNbt(ITEMS_KEY);
        return true;
    }

    public static void eraseAllBundledItems(ItemStack stack, PlayerEntity player){
        NbtCompound nbt = stack.getNbt();
        if(stack.isEmpty()){
            return;
        }

        ReignNoLonger.LOGGER.info("Tried");
        player.sendMessage(Text.of("§cThe endless abyss of oblivion was reset"), true);
        stack.removeSubNbt(ITEMS_KEY);
    }

    private static Stream<ItemStack> getBundledStacks(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null) {
            return Stream.empty();
        }

        NbtList items = nbt.getList(ITEMS_KEY, NbtElement.COMPOUND_TYPE);
        return items.stream()
                .filter(NbtCompound.class::isInstance)
                .map(NbtCompound.class::cast)
                .map(ItemStack::fromNbt);
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }
}
