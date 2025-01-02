package com.nitron.reign_no_longer.common.block;

import com.nitron.reign_no_longer.common.block.custom.functional.entities.SealOfDominionBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ReignNoLongerBlockEntities {
    public static BlockEntityType<SealOfDominionBlockEntity> SEAL_OF_DOMINION_BLOCK_ENTITY;

    public static void register() {
        SEAL_OF_DOMINION_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier("reign_no_longer", "seal_of_dominion_block_entity"),
                FabricBlockEntityTypeBuilder.create(SealOfDominionBlockEntity::new, ReignNoLongerBlocks.SEAL_OF_DOMINION).build(null)
        );
    }
}
