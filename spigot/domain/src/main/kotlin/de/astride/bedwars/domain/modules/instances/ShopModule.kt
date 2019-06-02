/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.shop.events.ShopEvents
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:36.
 * Last edit 01.06.2019
 */
object ShopModule : Module {

    override val dependencies: Set<Module> = setOf(ConfigModule)
    override var isRunning: Boolean = false

    private var shopEvents: ShopEvents? = null

    override fun setup(plugin: Plugin) {
        super.setup(plugin)
        shopEvents = ShopEvents()
        shopEvents?.setup(plugin)
    }

    override fun reset() {
        super.reset()
        shopEvents?.reset()
        shopEvents = null
    }

}