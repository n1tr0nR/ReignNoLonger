package com.nitron.reign_no_longer.common.item;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.item.custom.functional.AncientKeyItem;
import com.nitron.reign_no_longer.common.item.custom.functional.ForsakenMarkItem;
import com.nitron.reign_no_longer.common.item.custom.functional.FracturedReliquaryItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ReignNoLongerItems {

    public static Item FRACTURED_RELIQUARY = registerItem("fractured_reliquary", new FracturedReliquaryItem(new FabricItemSettings()));
    public static Item FORSAKEN_MARK = registerItem("forsaken_mark", new ForsakenMarkItem(new FabricItemSettings()));
    public static Item ANCIENT_KEY = registerItem("ancient_key", new AncientKeyItem(new FabricItemSettings()));


    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(ReignNoLonger.MOD_ID, name), item);
    }

    public static void init(){

    }
}
