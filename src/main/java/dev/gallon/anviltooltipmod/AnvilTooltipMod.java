/*
 * Copyright (C) 2022 @lilgallon on Github (Lilian Gallon)
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; version 2. This program is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
 * Public License along with this program.
 */

package dev.gallon.anviltooltipmod;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod("anviltooltipmod")
public class AnvilTooltipMod {
    public AnvilTooltipMod() {
        MinecraftForge.EVENT_BUS.addListener(this::addItemTooltip);
    }

    @SubscribeEvent
    public void addItemTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();

        if (itemStack.isEnchantable() || itemStack.isEnchanted() || (itemStack.getItem() instanceof EnchantedBookItem)) {
            // Repair cost = 2^n - 1 where n is the number of times the item has been previously worked
            int repairCost = itemStack.getBaseRepairCost();
            // So anvilUses = n = log_2(repairCost + 1) + 1 <- because we start at 1 and not 0
            int anvilUses = log2(repairCost + 1); // log base 2

            // Empty line
            event.getToolTip().add(Component.empty());

            // Number of enchantments
            event.getToolTip().add(
                    Component.translatable(Items.ANVIL.getDescriptionId())
                            .withStyle(ChatFormatting.GRAY)
                            .append(": " + anvilUses)
            );

            // Repair cost base
            event.getToolTip().add(
                    Component
                            .translatable("container.repair.cost", itemStack.getBaseRepairCost())
                            .withStyle(ChatFormatting.GRAY)
            );
        }
    }

    /**
     * My own log2 function because java only has log10.
     *
     * @param val the value to apply log2
     * @return the log2 result
     */
    private int log2(double val) {
        return (int) (Math.log(val) / Math.log(2) + 1e-10);
    }
}
