/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.events.BreakingHandler
import de.astride.bedwars.domain.modules.Module
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:54.
 * Last edit 30.05.2019
 */
object BreakingModule : Module {

    private var breakingHandler: BreakingHandler? = null

    override fun setup(plugin: Plugin) {
        breakingHandler = BreakingHandler()
        breakingHandler?.setup(plugin)
    }

    override fun reset() {
        breakingHandler?.reset()
    }

}