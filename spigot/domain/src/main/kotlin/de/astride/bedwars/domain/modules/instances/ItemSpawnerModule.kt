/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.itemspawner.ItemSpawners
import de.astride.bedwars.domain.modules.Module
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:43.
 * Last edit 30.05.2019
 */
object ItemSpawnerModule : Module {

    private var itemSpawners: ItemSpawners? = null

    override fun setup(plugin: Plugin) {
        itemSpawners =
            ItemSpawners()
    }

    override fun reset() {
        itemSpawners?.stop()
        itemSpawners = null
    }

}