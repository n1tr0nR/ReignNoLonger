package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.ReignNoLonger;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class SoulsRequiemItem extends Item {
    public SoulsRequiemItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            double userX = user.getX();
            double userY = user.getY();
            double userZ = user.getZ();

            Box area = new Box(userX - 100, userY - 100, userZ - 100, userX + 100, userY + 100, userZ + 100);

            world.getEntitiesByClass(LivingEntity.class, area, entity -> !entity.equals(user)).forEach(entity -> {
                entity.addStatusEffect(new StatusEffectInstance(ReignNoLonger.ENLIGHTENMENT, 100, 0));
            });
        }

        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
