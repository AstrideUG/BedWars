/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.shop.items

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:20.
 * Last edit 30.05.2019
 */
interface ShopItem {

    val display: ItemStack
    fun buy(player: Player, maxTimes: Int = 1)

}