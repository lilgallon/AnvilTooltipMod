/*
* Copyright (C) 2020 @N3ROO on Github (Lilian Gallon)
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation; version 2. This program is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
* PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
* Public License along with this program.
*/

package dev.nero.anviltooltipmod;

import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("anviltooltipmod")
public class AnvilTooltipMod
{
    public AnvilTooltipMod() {
        MinecraftForge.EVENT_BUS.addListener(this::addItemTooltip);
    }

    @SubscribeEvent
    public void addItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();

        if (itemStack.isEnchantable() || itemStack.isEnchanted() || (itemStack.getItem() instanceof EnchantedBookItem)) {
            // Repair cost = 2^n - 1 where n is the number of times the item has been previously worked
            int repairCost = itemStack.getRepairCost();
            // So anvilUses = n = log_2(repairCost + 1) + 1 <- because we start at 1 and not 0
            int anvilUses = log2(repairCost + 1); // log base 2

            // Empty line
            event.getToolTip().add(new TranslationTextComponent(""));

            // Number of enchantments
            event.getToolTip().add(
                    new TranslationTextComponent(Items.ANVIL.getTranslationKey())
                            .func_240699_a_(TextFormatting.GRAY)
                            .func_240702_b_(": " + anvilUses)
            );

            // Repair cost base
            event.getToolTip().add(
                    new TranslationTextComponent("container.repair.cost", itemStack.getRepairCost())
                            .func_240699_a_(TextFormatting.GRAY)
            );
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
