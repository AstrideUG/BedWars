/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain

import de.astride.bedwars.domain.teams.spectators
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 00:11.
 * Current Version: 1.0 (26.04.2019 - 01.06.2019)
 */

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 00:11.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */
val players: MutableCollection<Player> = mutableSetOf()

fun resetPlayers(plugin: Plugin) {
    players.clear()
    players += plugin.server.onlinePlayers - spectators
}
