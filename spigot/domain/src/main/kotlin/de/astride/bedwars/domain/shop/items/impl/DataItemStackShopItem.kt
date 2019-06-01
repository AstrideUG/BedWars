/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.shop.items.impl

import de.astride.bedwars.domain.shop.items.ItemStackShopItem
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:21.
 * Last edit 30.05.2019
 */
class DataItemStackShopItem(
    override val display: ItemStack,
    override val price: Int,
    override val money: ItemStack,
    override val items: Array<ItemStack>
) : ItemStackShopItem
