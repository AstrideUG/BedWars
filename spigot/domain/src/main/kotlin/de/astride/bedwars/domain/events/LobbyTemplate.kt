/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.events

import de.astride.bedwars.domain.players
import de.astride.bedwars.domain.services.ConfigService
import net.darkdevelopers.darkbedrock.darkness.spigot.countdowns.LobbyCountdown
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.events.countdown.LobbyCountdownCallEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.teams
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.LobbyEventsTemplate
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import de.astride.bedwars.domain.services.configService as configService0

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 05.05.2019 12:58.
 * Current Version: 1.0 (05.05.2019 - 01.06.2019)
 */
object LobbyTemplate : EventsTemplate() {

    fun setup(plugin: Plugin, configService: ConfigService = configService0) {
        val minPlayers = configService.lobbyMinPlayerToStart
        val countdown = LobbyCountdown(
            minPlayers = minPlayers,
            gameName = configService.lobbyGameName,
            players = players
        )

        LobbyEventsTemplate.setup(plugin, configService.lobbySpawn)
        listen<PlayerJoinEvent>(plugin) { if (players.size < minPlayers) countdown.idle() else countdown.start() }.add()
        listen<PlayerDisconnectEvent>(plugin) { if (players.isEmpty()) countdown.stop() else if (players.size < minPlayers) countdown.idle() }.add()
        listen<LobbyCountdownCallEvent>(plugin) { event ->
            val lobbyCountdown = event.lobbyCountdown

//            teams = generateTeams(10)

            if (lobbyCountdown.seconds == 10) {
//                            TODO load map
            }
            if (lobbyCountdown.seconds == 0) teams.forEach { team ->
                team.players.filter { it in lobbyCountdown.players }.forEach { it.teleport(team.location) }
            }
        }.add()
    }

}