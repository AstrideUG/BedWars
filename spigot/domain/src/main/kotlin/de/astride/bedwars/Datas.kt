/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars

import net.darkdevelopers.darkbedrock.darkness.spigot.team.GameTeam
import org.bukkit.Bukkit
import org.bukkit.entity.Player

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 00:11.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 00:11.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */
val players: Collection<Player> get() = Bukkit.getOnlinePlayers() //TODO: Bukkit.getOnlinePlayers().size - Saves.getTeamManager().getSpectators().size()

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 03:11.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */
lateinit var teams: Set<GameTeam>

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 05:51.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */
val Player.team: GameTeam? get() = teams.find { it.players.any { player -> player == this } }
