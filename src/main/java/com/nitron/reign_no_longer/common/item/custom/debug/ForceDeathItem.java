package com.nitron.reign_no_longer.common.item.custom.debug;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.block.ReignNoLongerBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.border.WorldBorder;

public class ForceDeathItem extends Item {
    public ForceDeathItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Block usedBlock = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        BlockPos p = context.getBlockPos();
        PlayerEntity e = context.getPlayer();

        if(usedBlock == ReignNoLongerBlocks.SEAL_OF_CONFINEMENT && e instanceof ServerPlayerEntity s){
            StatusEffectInstance i = new StatusEffectInstance(ReignNoLonger.ETERNAL_BIND, 100, 0, true, true, true);
            s.addStatusEffect(i);
            s.teleport(p.getX() + 0.5, p.getY() + 2, p.getZ() + 0.5);
            ServerWorld w = s.getServerWorld();
            WorldBorder b = w.getWorldBorder();
            b.setCenter(p.getX() + 0.5, p.getZ() + 0.5);
            b.interpolateSize(64, 1, 3000);
            try {
                wait(200);
                b.interpolateSize(1000000, 1, 500000);
            } catch (InterruptedException ex) {
                b.interpolateSize(1000000, 1, 500000);
                throw new RuntimeException(ex);
            }

            try {
                wait();
                b.interpolateSize(1000000, 1, 500000);
            } catch (InterruptedException ex) {
                b.interpolateSize(1000000, 1, 500000);
                throw new RuntimeException(ex);
            }
        }

        return super.useOnBlock(context);
    }
}
