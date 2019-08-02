/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.events.LobbyTemplate
import de.astride.bedwars.domain.functions.javaPlugin
import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.modules.disable
import de.astride.bedwars.domain.modules.install
import de.astride.bedwars.domain.players
import de.astride.bedwars.domain.resetPlayers
import net.darkdevelopers.darkbedrock.darkness.spigot.countdowns.PreGameCountdown
import net.darkdevelopers.darkbedrock.darkness.spigot.events.PlayerDisconnectEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.events.countdown.EndGameCountdownCallEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.events.countdown.LobbyCountdownCallEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.events.countdown.PreGameCountdownCallEvent
import net.darkdevelopers.darkbedrock.darkness.spigot.events.listener.EventsListener
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.InGameEventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.PreGameEventsTemplate
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
    private val inGame: Set<Module> = setOf(
        RespawnerModule,
        ItemSpawnerModule,
        ShopModule,
        BreakingModule
    )

    override fun setup(plugin: Plugin) {
        super.setup(plugin)

        EventsListener.autoRespawn = true

        LobbyTemplate.setup(plugin)

        listen<PlayerJoinEvent>(plugin) { resetPlayers(plugin) }.add()
        listen<PlayerDisconnectEvent>(plugin) { resetPlayers(plugin) }.add()

        val preGameCountdown = PreGameCountdown()
        listen<LobbyCountdownCallEvent>(plugin) {
            if (it.lobbyCountdown.seconds > 0) return@listen
            LobbyTemplate.reset()

            PreGameEventsTemplate.setup(plugin)
            preGameCountdown.start()
        }.add()
        listen<PreGameCountdownCallEvent>(plugin) {
            if (it.preGameCountdown.seconds > 0) return@listen
            preGameCountdown.stop()
            PreGameEventsTemplate.reset()

            InGameEventsTemplate.setup(plugin)
            inGame.install(plugin)
        }.add()
        listen<EndGameCountdownCallEvent>(plugin) {
            if (it.endGameCountdown.seconds > 0 && players.isNotEmpty()) return@listen
            plugin.server.shutdown()
        }.add()
    }

    override fun reset() {
        super<Module>.reset()
        super<EventsTemplate>.reset()
        LobbyTemplate.reset()
        PreGameEventsTemplate.reset()
        InGameEventsTemplate.reset()
        inGame.disable(javaPlugin.logger)
    }

}