package com.nitron.reign_no_longer.common.item;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.item.custom.debug.ForceDeathItem;
import com.nitron.reign_no_longer.common.item.custom.debug.ParticalBeamItem;
import com.nitron.reign_no_longer.common.item.custom.debug.ShockwaveParticleSpawnerItem;
import com.nitron.reign_no_longer.common.item.custom.functional.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ReignNoLongerItems {

    public static Item OBLIVION_POUCH = registerItem("oblivion_pouch", new OblivionPouchItem(new FabricItemSettings().maxCount(1)));
    public static Item FORSAKEN_MARK = registerItem("forsaken_mark", new ForsakenMarkItem(new FabricItemSettings().maxDamage(3)));
    public static Item WRIT_OF_PASSAGE = registerItem("writ_of_passage", new WritOfPassageItem(new FabricItemSettings().maxCount(1)));
    public static Item CONTRACT_STONE = registerItem("contract_stone", new ContractStoneItem(new FabricItemSettings().maxCount(1)));
    public static Item NULLIBLADE = registerItem("nulliblade", new VoidborneWeapons(ToolMaterials.NETHERITE, 10, -3, new FabricItemSettings()));
    public static Item PURE_BALANCE = registerItem("pure_balance", new VoidborneWeapons(ToolMaterials.NETHERITE, 15, -2, new FabricItemSettings()));
    public static Item FATES_SEVERANCE = registerItem("fates_severance", new FatesSeveranceItem());
    public static Item SHATTERED_SEVERANCE = registerItem("shattered_severance", new ShatteredSeveranceItem());

    //Debug
    public static Item DEBUG_BEAM = registerItem("debug_beam", new ParticalBeamItem(new FabricItemSettings()));
    public static Item DEBUG_KILL = registerItem("debug_kill", new ForceDeathItem(new FabricItemSettings()));
    public static Item DEBUG_SHOCKWAVE = registerItem("debug_shockwave", new ShockwaveParticleSpawnerItem(new FabricItemSettings()));

    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(ReignNoLonger.MOD_ID, name), item);
    }

    public static void init(){

    }
}
