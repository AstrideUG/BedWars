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
 * Last edit 30.05.2019
 */
object RespawnerModule : Module {

    override fun setup(plugin: Plugin): Unit = RespawnerEvents.setup(
        plugin,
        TeamRespawners()
    )

    override fun reset(): Unit = RespawnerEvents.reset()

}