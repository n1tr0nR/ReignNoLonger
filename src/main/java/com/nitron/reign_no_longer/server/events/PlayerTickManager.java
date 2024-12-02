package com.nitron.reign_no_longer.server.events;

import com.nitron.reign_no_longer.ReignNoLonger;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItemGroup;
import com.nitron.reign_no_longer.common.item.ReignNoLongerItems;
import com.nitron.reign_no_longer.common.item.custom.base.RNLPermaItem;
import com.nitron.reign_no_longer.common.item.custom.base.RNLPermaSword;
import com.nitron.reign_no_longer.common.item.custom.functional.OblivionPouchItem;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;


public class PlayerTickManager {
    public static void registerEvents(){
        ServerLivingEntityEvents.ALLOW_DEATH.register((entity, damageSource, context) -> {
            if(entity instanceof PlayerEntity killedPlayer){
                PlayerInventory inventory = killedPlayer.getInventory();
                PlayerEntity Player = (PlayerEntity) damageSource.getAttacker();
                ItemStack mainhandItem = Player.getStackInHand(Hand.MAIN_HAND);
                ItemStack offhandItem = Player.getStackInHand(Hand.OFF_HAND);

                if(mainhandItem.getItem() instanceof RNLPermaSword){
                    if(!entity.hasStatusEffect(ReignNoLonger.ETERNAL_BIND)){
                        return trapPlayer(killedPlayer);
                    } else {
                        return killPlayer(killedPlayer);
                    }
                } else if(offhandItem.getItem() instanceof RNLPermaItem){
                    if(offhandItem.isOf(ReignNoLongerItems.FORSAKEN_MARK))
                    {
                        if(offhandItem.getDamage() >= 2){
                            createExplosion(killedPlayer.getWorld(), killedPlayer.getBlockPos(), 10, false);
                        }
                        offhandItem.damage(1, Player, (player) -> player.sendToolBreakStatus(Hand.OFF_HAND));
                    }
                    return killPlayer(killedPlayer);
                }

                oblivionPouchErase(killedPlayer);
            }
            return true;
        });
    }

    private static void oblivionPouchErase(PlayerEntity player){
        ItemStack stack = getItemInInventory(player, ReignNoLongerItems.OBLIVION_POUCH);
        OblivionPouchItem.eraseAllBundledItems(stack, player);
    }

    private static boolean trapPlayer(PlayerEntity player){
        World world = player.getWorld();

        player.setHealth(player.getMaxHealth());
        player.addStatusEffect(new StatusEffectInstance(ReignNoLonger.ETERNAL_BIND, 999999, 0, true, true, true));
        for (PlayerEntity pe : world.getPlayers()){
            pe.sendMessage(Text.of("§c" + player.getEntityName() + "'s soul is in danger..."), true);
        }
        return false;
    }

    private static boolean killPlayer(PlayerEntity player){
        World world = player.getWorld();

        player.clearStatusEffects();
        player.setHealth(player.getMaxHealth());
        if(player instanceof ServerPlayerEntity serverPlayer){
            serverPlayer.changeGameMode(GameMode.SPECTATOR);

            for (PlayerEntity pe : world.getPlayers()){
                pe.sendMessage(Text.of(serverPlayer.getEntityName() + " was erased from existence"));
                pe.sendMessage(Text.of("§e" + serverPlayer.getEntityName() + " left the game"));
            }
        }
        return false;
    }

    public static ItemStack getItemInInventory(PlayerEntity player, Item targetItem){
        PlayerInventory inventory = player.getInventory();

        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);

            if (stack.isOf(targetItem)) {
                ReignNoLonger.LOGGER.info("Found item of name: " + targetItem.getName() + " in slot id: " + i);
                return stack;
            }
        }
        ReignNoLonger.LOGGER.info("Nothing Found");
        return ItemStack.EMPTY;
    }

    public static void createExplosion(World world, BlockPos pos, float strength, boolean doFire) {
        if (!world.isClient) {
            world.createExplosion(
                    null,
                    null,
                    null,
                    pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
                    strength,
                    doFire,
                    World.ExplosionSourceType.TNT
            );
        }
    }
}
