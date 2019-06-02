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
 * Last edit 01.06.2019
 */
object BreakingModule : Module {

    override val dependencies: Set<Module> = setOf(ConfigModule)
    override var isRunning: Boolean = false

    private var breakingHandler: BreakingHandler? = null

    override fun setup(plugin: Plugin) {
        super.setup(plugin)
        breakingHandler = BreakingHandler()
        breakingHandler?.setup(plugin)
    }

    override fun reset() {
        super.reset()
        breakingHandler?.reset()
    }

}