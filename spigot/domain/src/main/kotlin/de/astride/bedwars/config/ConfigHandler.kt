/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.config

import org.bukkit.Material

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 27.05.2019 01:24.
 * Last edit 27.05.2019
 */
object ConfigHandler {

    class Vars(map: Map<String, Any?>) {

        val delay: Int by map.withDefault { 10 }
        val breakingHandler: List<Material> by map.withDefault { listOf(Material.DOUBLE_PLANT, Material.LONG_GRASS) }

    }

}