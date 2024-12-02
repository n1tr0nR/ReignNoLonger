package com.nitron.reign_no_longer.common.item.custom.debug;

import com.nitron.reign_no_longer.lodestone.effects.ExampleParticleEffect;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticalBeamItem extends Item {
    public ParticalBeamItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        TIME = 0;
        death(world, pos.toCenterPos(), 1000);
        return super.useOnBlock(context);
    }

    private static long TIME;

    private void death(World world, Vec3d pos, int time){
        ServerTickEvents.END_SERVER_TICK.register(serverWorld -> {
            if(TIME <= time) {
                ExampleParticleEffect.spawnExampleParticles(world, pos);
                TIME++;
            }
        });
    }
}
