/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.shop.categories

import de.astride.bedwars.domain.shop.items.ShopItem
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:21.
 * Last edit 30.05.2019
 */
interface ShopCategory : Comparable<ShopCategory> {
    val display: ItemStack
    val items: Collection<ShopItem>
    val priority: Int get() = 0

    override fun compareTo(other: ShopCategory): Int = priority - other.priority
}