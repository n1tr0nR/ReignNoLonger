package com.nitron.reign_no_longer.common.block.custom.functional.entities;

import com.nitron.reign_no_longer.common.block.ReignNoLongerBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SealOfDominionBlockEntity extends BlockEntity {
    private UUID owner;
    public static final Map<UUID, GameMode> playerGameModes = new HashMap<>();

    public SealOfDominionBlockEntity(BlockPos pos, BlockState state) {
        super(ReignNoLongerBlockEntities.SEAL_OF_DOMINION_BLOCK_ENTITY, pos, state);
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean isOwner(ServerPlayerEntity player) {
        return player.getUuid().equals(owner);
    }

    public Map<UUID, GameMode> getPlayerGameModes() {
        return playerGameModes;
    }
}

