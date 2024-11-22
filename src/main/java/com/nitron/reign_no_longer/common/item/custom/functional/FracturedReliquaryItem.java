package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.ReignNoLonger;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FracturedReliquaryItem extends Item {
    public FracturedReliquaryItem(Settings settings) {
        super(settings.maxDamage(2));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient && user.hasStatusEffect(ReignNoLonger.ETERNAL_BIND)){
            ItemStack stack = user.getStackInHand(hand);
            stack.damage(1, user, (player) -> user.sendToolBreakStatus(user.getActiveHand()));
            user.sendMessage(Text.of("§eYour soul was spared today..."));
            user.removeStatusEffect(ReignNoLonger.ETERNAL_BIND);
            world.playSound(null, BlockPos.ofFloored(user.getPos()), SoundEvents.PARTICLE_SOUL_ESCAPE, SoundCategory.PLAYERS, 10f, 1f);
        } else if(!world.isClient){
            user.sendMessage(Text.of("§cYour soul is not in danger..."));
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.translatable("tooltip.reign_no_longer.fractured_reliquary"));
        super.appendTooltip(stack, world, tooltip, context);
    }

    @Override
    public boolean isDamageable() {
        return true;
    }
}
