/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.teams.TeamRespawners
import de.astride.bedwars.domain.teams.events.RespawnerEvents
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:51.
 * Last edit 01.06.2019
 */
object RespawnerModule : Module {

    override val dependencies: Set<Module> = setOf(ConfigModule)
    override var isRunning: Boolean = false

    override fun setup(plugin: Plugin) {
        super.setup(plugin)
        RespawnerEvents.setup(plugin, TeamRespawners())
    }

    override fun reset() {
        super.reset()
        RespawnerEvents.reset()
    }

}