package com.nitron.reign_no_longer.common.block;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.block.custom.functional.SoulBoundAltar;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ReignNoLongerBlocks {

    public static Block SOUL_BOUND_ALTAR = genBlock("soul_bound_altar", new SoulBoundAltar(FabricBlockSettings.copyOf(Blocks.BEDROCK).nonOpaque().postProcess(Blocks::always).emissiveLighting(Blocks::always).luminance(2)));

    private static Block genBlock(String name, Block block){
        genBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(ReignNoLonger.MOD_ID, name), block);
    }

    private static void genBlockItem(String name, Block block){
        Registry.register(Registries.ITEM, new Identifier(ReignNoLonger.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void init(){

    }
}
