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
 * Last edit 01.06.2019
 */
object ItemSpawnerModule : Module {

    override val dependencies: Set<Module> = setOf(ConfigModule)
    override var isRunning: Boolean = false

    private var itemSpawners: ItemSpawners? = null

    override fun setup(plugin: Plugin) {
        super.setup(plugin)
        itemSpawners = ItemSpawners()
    }

    override fun reset() {
        super.reset()
        itemSpawners?.stop()
        itemSpawners = null
    }

}