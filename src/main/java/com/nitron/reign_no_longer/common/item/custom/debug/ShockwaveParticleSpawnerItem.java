package com.nitron.reign_no_longer.common.item.custom.debug;

import com.nitron.reign_no_longer.ReignNoLonger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class ShockwaveParticleSpawnerItem extends Item {
    public ShockwaveParticleSpawnerItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Vec3d pos = context.getHitPos();
        BlockPos posB = context.getBlockPos();
        PlayerEntity player = context.getPlayer();
        if(player instanceof ServerPlayerEntity playerEntity){
            ServerWorld world = playerEntity.getServerWorld();
            world.spawnParticles(ReignNoLonger.IMPLODE, pos.getX(), pos.getY() + 0.1f, pos.getZ(), 1, 0, 0, 0, 0);
            world.spawnParticles(ReignNoLonger.RUNE, pos.getX(), pos.getY() + 0.1f, pos.getZ(), 1, 0, 0, 0, 0);
            world.spawnParticles(ReignNoLonger.IMPLODEB, pos.getX(), pos.getY(), pos.getZ(), 3, 0, 0, 0, 0);
            world.playSound(null, posB, ReignNoLonger.implode_charge, SoundCategory.PLAYERS, 10, 1);
        }
        return super.useOnBlock(context);
    }
}
