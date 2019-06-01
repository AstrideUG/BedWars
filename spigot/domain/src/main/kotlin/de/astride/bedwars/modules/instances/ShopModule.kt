/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.modules.instances

import de.astride.bedwars.modules.Module
import de.astride.bedwars.shop.events.ShopEvents
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:36.
 * Last edit 30.05.2019
 */
object ShopModule : Module {

    private var shopEvents: ShopEvents? = null

    override fun setup(plugin: Plugin) {
        shopEvents = ShopEvents()
        shopEvents?.setup(plugin)
    }

    override fun reset() {
        shopEvents?.reset()
    }

}