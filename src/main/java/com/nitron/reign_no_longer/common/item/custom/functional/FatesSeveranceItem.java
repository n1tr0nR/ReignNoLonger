package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.item.custom.ReignNoLongerToolMaterials;
import com.nitron.reign_no_longer.common.item.custom.base.RNLPermaSword;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class FatesSeveranceItem extends RNLPermaSword {
    public FatesSeveranceItem() {
        super(ReignNoLongerToolMaterials.UNHOLY, 5, -2.4F, new FabricItemSettings());
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        Random random = new Random();
        attacker.getWorld().playSound(null, BlockPos.ofFloored(new Vec3d(target.getX(), target.getY(), target.getZ())),
                ReignNoLonger.fates_severance_hit, SoundCategory.PLAYERS, 1F, 0.5F + random.nextFloat() * 0.2F);
        return super.postHit(stack, target, attacker);
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
}
