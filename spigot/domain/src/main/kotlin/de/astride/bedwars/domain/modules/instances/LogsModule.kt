/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.action.consume
import de.astride.bedwars.domain.action.unregister
import de.astride.bedwars.domain.functions.toDefaultBlockLocation
import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.players
import de.astride.bedwars.domain.teams.TeamRespawners
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.*
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Colors
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Messages
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:09.
 * Last edit 30.05.2019
 */
object LogsModule : Module {

    override fun setup(plugin: Plugin) {

        val prefix = "${plugin.name}-"
        team("${prefix}TeamRespawner")
        shop("${prefix}Shop")

    }

    override fun reset(): Unit = this.unregister()

    private fun team(group: String): Unit = "team.respawner.destroyed.by".consume(group) { vars ->

        val event = vars["event"] as? BlockBreakEvent ?: return@consume
        val teamRespawners = vars["teamRespawners"] as? TeamRespawners ?: return@consume

        val locationTeam = teamRespawners.getTeam(event.block.location.toDefaultBlockLocation()) ?: return@consume
        if (teamRespawners.hasRespawner(locationTeam)) return@consume

        val player: Player = event.player
        val chatColor = player.team?.chatColor ?: ChatColor.WHITE
        players.forEach { players ->
            players.sendMessage(
                "${Messages.PREFIX}${Colors.TEXT}$chatColor${player.displayName}${Colors.TEXT}" +
                        " hat das Bett von " +
                        "${locationTeam.chatColor}${locationTeam.name}${Colors.TEXT}" +
                        " abgebaut"
            )
            if (players.team == locationTeam) {
                players.sendTitle("${Colors.IMPORTANT}Deine Respawner")
                players.sendSubTitle("${Colors.TEXT}wurde abgebaut")
                players.sendTimings(0, 40, 5)
            } else {
                players.sendTitle("${Colors.IMPORTANT}Die Respawner von")
                players.sendSubTitle("${locationTeam.chatColor}${locationTeam.name} ${Colors.TEXT}wurde abgebaut")
                players.sendTimings(0, 40, 5)
            }
            players.sendInGameScoreBoard(teamRespawners)
        }
        //"${Messages.PREFIX}${Colors.TEXT}Du kannst dein eigenes Bett nicht ${Colors.IMPORTANT}abbauen"
        //"${Messages.PREFIX}${Colors.TEXT}${playerTeam.chatColor}${player.displayName}${Colors.TEXT} hat das Bett von ${locationTeam.chatColor}${locationTeam.name}${Colors.TEXT} abgebaut"
    }

    private fun shop(group: String) {
        "shop.item.buy.successfully".consume(group) { vars ->

            val player = vars["player"] as? Player ?: return@consume
            player.playSound(player.location, Sound.ITEM_PICKUP, 1.0f, 1.0f)

        }

        "shop.item.buy.not.enough.money".consume(group) { vars ->

            val player = vars["player"] as? Player ?: return@consume
            val money = vars["money"] as? ItemStack ?: return@consume

            player.sendMessage("${Messages.PREFIX}${Colors.TEXT}Du besitzt nicht genug ${Colors.IMPORTANT}$money")
            player.playSound(player.location, Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f)

        }
    }


    private fun Player.sendInGameScoreBoard(teamRespawners: TeamRespawners) {
        val displayName =
            "${Colors.PRIMARY}${Colors.EXTRA}SERVERNAME${Colors.IMPORTANT}${Colors.EXTRA}.${Colors.PRIMARY}${Colors.EXTRA}NET"
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

