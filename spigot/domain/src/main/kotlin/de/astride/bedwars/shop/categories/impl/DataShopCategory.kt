/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.shop.categories.impl

import de.astride.bedwars.shop.categories.ShopCategory
import de.astride.bedwars.shop.items.ShopItem
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:22.
 * Last edit 30.05.2019
 */
data class DataShopCategory(
    override val display: ItemStack,
    override val items: Collection<ShopItem>,
    override val priority: Int = 0
) : ShopCategory
