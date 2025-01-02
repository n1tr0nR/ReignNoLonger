package com.nitron.reign_no_longer.common.item.custom.functional;

import com.nitron.reign_no_longer.interfaces.Permakilling;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.util.Hand;

public class VoidborneWeapons extends SwordItem implements Permakilling {
    public VoidborneWeapons(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean binding() {
        return false;
    }

    @Override
    public Hand usable() {
        return Hand.MAIN_HAND;
    }

    @Override
    public boolean breakable() {
        return false;
    }
}
