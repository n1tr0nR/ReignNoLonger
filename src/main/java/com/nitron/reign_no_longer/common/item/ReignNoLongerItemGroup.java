package com.nitron.reign_no_longer.common.item;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.block.ReignNoLongerBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ReignNoLongerItemGroup {

    public static final ItemGroup RNL_ITEMS = Registry.register(Registries.ITEM_GROUP,
            new Identifier(ReignNoLonger.MOD_ID, "rnl_items"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.reign_no_longer.items"))
                    .icon(() -> new ItemStack(ReignNoLongerItems.FATES_SEVERANCE)).entries((displayContext, entries) -> {
                        entries.add(ReignNoLongerItems.FORSAKEN_MARK);
                        entries.add(ReignNoLongerItems.SHATTERED_SEVERANCE);
                        entries.add(ReignNoLongerItems.FATES_SEVERANCE);
                        entries.add(ReignNoLongerItems.WRIT_OF_PASSAGE);
                        entries.add(ReignNoLongerItems.CONTRACT_STONE);
                        entries.add(ReignNoLongerItems.SACRED_CHISEL);
                        entries.add(ReignNoLongerBlocks.SEAL_OF_CONFINEMENT);
                        entries.add(ReignNoLongerItems.OBLIVION_POUCH);
                    }).build()
    );

    public static void init(){
    }

}
