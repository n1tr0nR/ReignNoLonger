package com.nitron.reign_no_longer.interfaces;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;

public interface Permakilling {
    boolean binding();
    Hand usable();
    boolean breakable();
}
