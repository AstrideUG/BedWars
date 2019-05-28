/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.events

import de.astride.bedwars.players
import net.darkdevelopers.darkbedrock.darkness.spigot.countdowns.LobbyCountdown
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.events.countdown.LobbyCountdownCallEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.teams
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.LobbyEventsTemplate
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 05.05.2019 12:58.
 * Current Version: 1.0 (05.05.2019 - 26.05.2019)
 */
object LobbyTemplate : EventsTemplate() {

    fun setup(plugin: Plugin) {
        startLobby(plugin)
    }

    /* TODO change defaults to config */
    private fun startLobby(
        plugin: Plugin,
        spawn: Location = Location(Bukkit.getWorlds()[0], 0.0, 100.0, 0.0),
        minPlayers: Int = 2,
        gameName: String = ""
    ) {
        val countdown = LobbyCountdown(minPlayers = minPlayers, gameName = gameName, players = players)

        LobbyEventsTemplate.setup(plugin, spawn)
        listen<PlayerJoinEvent>(plugin) { if (players.size < minPlayers) countdown.idle() else countdown.start() }.add()
        listen<PlayerDisconnectEvent>(plugin) { if (players.isEmpty()) countdown.stop() else if (players.size < minPlayers) countdown.idle() }.add()
        listen<LobbyCountdownCallEvent>(plugin) { event ->
            val lobbyCountdown = event.lobbyCountdown
            //            if (lobbyCountdown.seconds == 10) TODO load map
            teams.forEach { team ->
                team.players.filter { it in lobbyCountdown.players }.forEach { it.teleport(team.location) }
            }
            if (lobbyCountdown.seconds == 0) lobbyCountdown.players.forEach { it.teleport(spawn) }
        }.add()
    }


}