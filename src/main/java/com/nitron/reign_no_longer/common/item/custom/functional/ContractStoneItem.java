package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.ReignNoLonger;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ContractStoneItem extends Item {
    public ContractStoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        NbtCompound nbt = stack.getOrCreateNbt();
        if(!nbt.getBoolean("signed")){
            nbt.putString("player_name", "PLAYERNAME");
            nbt.putBoolean("signed", true);
            nbt.putString("stored_username", user.getName().getString());
            nbt.putString("stored_uuid", user.getUuidAsString());
            user.swingHand(hand);
            world.playSound(null, BlockPos.ofFloored(user.getPos()), SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, SoundCategory.PLAYERS);
        }
        return super.use(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if(stack.getOrCreateNbt().getBoolean("signed")) {
            tooltip.add(Text.of("§8§kAAAAAAAAAAAAAAAAA"));
            tooltip.add(Text.of("§7Signed By:§6 " + stack.getOrCreateNbt().getString("stored_username")));
            tooltip.add(Text.of("§8§kAAAAAAAAAAAAAAAAA"));
        } else {
            tooltip.add(Text.translatable("tooltip.reign_no_longer.contract_stone_unsigned"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
