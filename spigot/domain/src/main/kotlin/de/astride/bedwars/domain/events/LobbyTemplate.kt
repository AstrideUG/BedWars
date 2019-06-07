/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.events

import de.astride.bedwars.domain.players
import de.astride.bedwars.domain.services.configService
import net.darkdevelopers.darkbedrock.darkness.spigot.countdowns.LobbyCountdown
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.events.countdown.LobbyCountdownCallEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.loadBukkitWorld
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.schedule
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.teams
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.LobbyEventsTemplate
import org.bukkit.Location
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import kotlin.random.Random

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 05.05.2019 12:58.
 * Current Version: 1.0 (05.05.2019 - 07.06.2019)
 */
object LobbyTemplate : EventsTemplate() {

    private val minPlayers = configService.lobbyMinPlayerToStart
    val countdown = LobbyCountdown(
        minPlayers = minPlayers,
        gameName = configService.lobbyGameName,
        players = players
    )

    fun setup(plugin: Plugin) {

        LobbyEventsTemplate.setup(plugin, configService.lobbySpawn)
        listen<PlayerJoinEvent>(plugin) { if (players.size < minPlayers) countdown.idle() else countdown.start() }.add()
        listen<PlayerDisconnectEvent>(plugin) { if (players.isEmpty()) countdown.stop() else if (players.size < minPlayers) countdown.idle() }.add()
        listen<LobbyCountdownCallEvent>(plugin) { event ->
            val lobbyCountdown = event.lobbyCountdown

//            teams = generateTeams(10)
            plugin.schedule {
                val world by lazy {
                    val worlds = configService.worlds.toList()
                    when {
                        worlds.isEmpty() -> "GeneratedGameWorld"
                        worlds.size < 2 -> worlds[0]
                        else -> worlds[Random.nextInt(worlds.size - 1)]
                    }.loadBukkitWorld()
                }
                if (lobbyCountdown.seconds == 10) world
                if (lobbyCountdown.seconds == 0) {
                    players.forEach { it.teleport(Location(world, 0.0, 100.0, 0.0)) }
                    teams.forEach { team ->
                        team.players.filter { it in lobbyCountdown.players }.forEach { it.teleport(team.location) }
                    }
                }
            }
        }.add()
    }

    override fun reset() {
        super.reset()
        countdown.stop()
    }

}