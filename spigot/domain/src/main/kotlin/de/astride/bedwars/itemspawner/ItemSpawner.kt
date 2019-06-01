/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.itemspawner

import org.bukkit.Location
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:43.
 * Last edit 30.05.2019
 */
interface ItemSpawner {

    val drop: ItemStack
    val location: Set<Location>
    @Suppress("EXPERIMENTAL_API_USAGE")
    val allowed: (UInt) -> Boolean

}