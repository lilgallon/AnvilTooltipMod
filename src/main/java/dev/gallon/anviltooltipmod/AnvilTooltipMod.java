/*
* Copyright (C) 2020 @N3ROO on Github (Lilian Gallon)
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation; version 2. This program is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
* PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
* Public License along with this program.
*/

package dev.gallon.anviltooltipmod;

import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = AnvilTooltipMod.MODID, name = AnvilTooltipMod.NAME, version = AnvilTooltipMod.VERSION)
public class AnvilTooltipMod
{
    public static final String MODID = "anviltooltipmod";
    public static final String NAME = "Anvil Tooltip Mod";
    public static final String VERSION = "1.0.0";

    public AnvilTooltipMod() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void addItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();

        if (itemStack.isItemEnchantable() || itemStack.isItemEnchanted() || (itemStack.getItem() instanceof ItemEnchantedBook)) {
            // Repair cost = 2^n - 1 where n is the number of times the item has been previously worked
            int repairCost = itemStack.getRepairCost();
            // So anvilUses = n = log_2(repairCost + 1) + 1 <- because we start at 1 and not 0
            int anvilUses = log2(repairCost + 1); // log base 2

            // Empty line
            event.getToolTip().add("");

            // Number of enchantments
            event.getToolTip().add(Blocks.ANVIL.getLocalizedName() + ": " + anvilUses);

            // Repair cost base (not relevant in 1.12.2)
            // event.getToolTip().add(I18n.format("container.repair.cost", itemStack.getRepairCost()));
        }
    }

    /**
     * My own log2 function because java only has log10.
     * @param val the value to apply log2
     * @return the log2 result
     */
    private int log2(double val) {
        return (int) (Math.log(val) / Math.log(2) + 1e-10);
    }
}
