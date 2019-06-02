/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.events.LobbyTemplate
import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.resetPlayers
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:37.
 * Last edit 01.06.2019
 */
object GameStatesModule : Module, EventsTemplate() {

    override var isRunning: Boolean = false
    override val dependencies: Set<Module> = setOf(ConfigModule)

    override fun setup(plugin: Plugin) {
        super.setup(plugin)
        LobbyTemplate.setup(plugin)
        listen<PlayerJoinEvent>(plugin) { resetPlayers(plugin) }.add()
        listen<PlayerDisconnectEvent>(plugin) { resetPlayers(plugin) }.add()
    }

    override fun reset() {
        super<Module>.reset()
        super<EventsTemplate>.reset()
        LobbyTemplate.reset()
    }

}