/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.players
import de.astride.bedwars.domain.teams.TeamRespawners
import net.darkdevelopers.darkbedrock.darkness.general.action.consume
import net.darkdevelopers.darkbedrock.darkness.general.action.unregister
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.messages
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.*
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.to.toDefaultBlockLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Colors.*
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:09.
 * Last edit 01.06.2019
 */
object LogsModule : Module {

    override var isRunning: Boolean = false

    override fun setup(plugin: Plugin) {
        super.setup(plugin)

        val prefix = "${plugin.name}-"
        team("${prefix}TeamRespawner")
        shop("${prefix}Shop")

    }

    override fun reset() {
        super.reset()
        unregister()
    }

    private fun team(group: String): Unit = "team.respawner.destroyed.by".consume(group) { vars ->

        val event = vars["event"] as? BlockBreakEvent ?: return@consume
        val teamRespawners = vars["teamRespawners"] as? TeamRespawners ?: return@consume

        val locationTeam = teamRespawners.getTeam(event.block.location.toDefaultBlockLocation()) ?: return@consume
        if (teamRespawners.hasRespawner(locationTeam)) return@consume

        val player: Player = event.player
        val chatColor = player.team?.chatColor ?: ChatColor.WHITE
        players.forEach { players ->
            players.sendMessage(
                "${messages.prefix}$TEXT$chatColor${player.displayName}$TEXT" +
                        " hat das Bett von " +
                        "${locationTeam.chatColor}${locationTeam.name}$TEXT" +
                        " abgebaut"
            )
            if (players.team == locationTeam) {
                players.sendTitle("${IMPORTANT}Deine Respawner")
                players.sendSubTitle("${TEXT}wurde abgebaut")
                players.sendTimings(0, 40, 5)
            } else {
                players.sendTitle("${IMPORTANT}Die Respawner von")
                players.sendSubTitle("${locationTeam.chatColor}${locationTeam.name} ${TEXT}wurde abgebaut")
                players.sendTimings(0, 40, 5)
            }
            players.sendInGameScoreBoard(teamRespawners)
        }
        //"${net.darkdevelopers.darkbedrock.darkness.spigot.configs.messages.prefix}${Colors.TEXT}Du kannst dein eigenes Bett nicht ${Colors.IMPORTANT}abbauen"
        //"${net.darkdevelopers.darkbedrock.darkness.spigot.configs.messages.prefix}${Colors.TEXT}${playerTeam.chatColor}${player.displayName}${Colors.TEXT} hat das Bett von ${locationTeam.chatColor}${locationTeam.name}${Colors.TEXT} abgebaut"
    }

    private fun shop(group: String) {
        "shop.item.buy.successfully".consume(group) { vars ->

            val player = vars["player"] as? Player ?: return@consume
            player.playSound(player.location, Sound.ITEM_PICKUP, 1.0f, 1.0f)

        }

        "shop.item.buy.not.enough.money".consume(group) { vars ->

            val player = vars["player"] as? Player ?: return@consume
            val money = vars["money"] as? ItemStack ?: return@consume

            player.sendMessage("${messages.prefix}${TEXT}Du besitzt nicht genug $IMPORTANT$money")
            player.playSound(player.location, Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f)

        }
    }


    private fun Player.sendInGameScoreBoard(teamRespawners: TeamRespawners) {
        val displayName = "$PRIMARY${EXTRA}SERVERNAME$IMPORTANT$EXTRA.$PRIMARY${EXTRA}NET"
        val scores = mutableSetOf<Pair<String, Int>>().apply {
            teams.forEach { team ->
                val s = "${team.chatColor}${team.name}"
                val s1 = if (teamRespawners.hasRespawner(team)) "§a✔" else "§4✗"
                val s2 =
                    if (team.players.isEmpty()) "${ChatColor.DARK_GRAY}${ChatColor.stripColor(s1)}${ChatColor.GRAY}$s" else "$s1$s"
                add(s2 to team.size)
            }
        }.toMap().toScoreboardScore().toSet()
        sendScoreBoard(displayName, scores)
    }

}

