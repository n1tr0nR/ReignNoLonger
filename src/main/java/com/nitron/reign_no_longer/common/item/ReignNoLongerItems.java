package com.nitron.reign_no_longer.common.item;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.item.custom.debug.ForceDeathItem;
import com.nitron.reign_no_longer.common.item.custom.debug.ParticalBeamItem;
import com.nitron.reign_no_longer.common.item.custom.functional.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ReignNoLongerItems {

    public static Item FRACTURED_RELIQUARY = registerItem("fractured_reliquary", new FracturedReliquaryItem(new FabricItemSettings()));
    public static Item OBLIVION_POUCH = registerItem("oblivion_pouch", new OblivionPouchItem(new FabricItemSettings().maxCount(1)));
    public static Item FORSAKEN_MARK = registerItem("forsaken_mark", new ForsakenMarkItem(new FabricItemSettings().maxDamage(3)));
    public static Item WRIT_OF_PASSAGE = registerItem("writ_of_passage", new WritOfPassageItem(new FabricItemSettings().maxCount(1)));
    public static Item CONTRACT_STONE = registerItem("contract_stone", new ContractStoneItem(new FabricItemSettings().maxCount(1)));
    public static Item SACRED_CHISEL = registerItem("sacred_chisel", new SwordItem(ToolMaterials.NETHERITE, 3, -3, new FabricItemSettings()));
    public static Item FATES_SEVERANCE = registerItem("fates_severance", new FatesSeveranceItem());
    public static Item HOLLOWED_HEART = registerItem("hollowed_heart", new HollowedHeart(new FabricItemSettings()));
    public static Item SOULS_REQUIEM = registerItem("souls_requiem", new SoulsRequiemItem(new FabricItemSettings()));


    //Debug
    public static Item DEBUG_BEAM = registerItem("debug_beam", new ParticalBeamItem(new FabricItemSettings()));
    public static Item DEBUG_KILL = registerItem("debug_kill", new ForceDeathItem(new FabricItemSettings()));


    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(ReignNoLonger.MOD_ID, name), item);
    }

    public static void init(){

    }
}
