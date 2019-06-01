/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.itemspawner.impl

import de.astride.bedwars.domain.itemspawner.ItemSpawner
import org.bukkit.Location
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:43.
 * Last edit 30.05.2019
 */
data class DataItemSpawner(
    override val drop: ItemStack,
    override val location: Set<Location>,
    @Suppress("EXPERIMENTAL_API_USAGE")
    override val allowed: (UInt) -> Boolean
) : ItemSpawner