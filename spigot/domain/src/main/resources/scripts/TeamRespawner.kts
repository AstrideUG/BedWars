import de.astride.bedwars.functions.equalsInt
import de.astride.bedwars.functions.javaPlugin
import de.astride.bedwars.players
import de.astride.bedwars.team
import de.astride.bedwars.teams
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.*
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Colors.*
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Messages
import net.darkdevelopers.darkbedrock.darkness.spigot.team.GameTeam
import org.bukkit.ChatColor.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin
import javax.script.ScriptEngine


/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 02:57.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */

lateinit var listener: Listener

@Suppress("unused") //called by script loader
fun hooking(engine: ScriptEngine) {

    val respawners: Set<TeamRespawner> = teams.map { DataTeamRespawner(it, setOf() /* TODO make it configurable */) }.toSet()
    listener = TeamRespawnerListener(engine.javaPlugin, TeamRespawners(respawners))

}

fun stop(): Unit = listener.unregister()

class TeamRespawnerListener(
    javaPlugin: JavaPlugin,
    private val teamRespawners: TeamRespawners
) : Listener(javaPlugin) {

    @EventHandler
    fun on(event: BlockBreakEvent) {

        val player: Player = event.player
        val location: Location = event.block.location

        teamRespawners.breakRespawner(player, location)

        val locationTeam = teamRespawners.getTeam(location) ?: return
        if (teamRespawners.hasRespawner(locationTeam)) return

        val chatColor = player.team?.chatColor ?: WHITE
        players.forEach { players ->
            players.sendMessage(
                "${Messages.PREFIX}$TEXT$chatColor${player.displayName}$TEXT" +
                        " hat das Bett von " +
                        "${locationTeam.chatColor}${locationTeam.name}$TEXT" +
                        " abgebaut"
            )
            if (players.team == locationTeam) {
                players.sendTitle("${IMPORTANT}Dein Bett")
                players.sendSubTitle("${TEXT}wurde abgebaut")
                players.sendTimings(0, 40, 5)
            } else {
                players.sendTitle("${IMPORTANT}Das Bett von")
                players.sendSubTitle("${locationTeam.chatColor}${locationTeam.name} ${TEXT}wurde abgebaut")
                players.sendTimings(0, 40, 5)
            }
        }
//        if (locationTeam.players.size == 0) Saves.getTeamManager().removeTeam(locationTeam)

        players.forEach { it.sendInGameScoreBoard(teamRespawners) }
//            Saves.getCoinsAPI().addCoins(player.uniqueId, 50, Messages.getName())

    }

    private fun Player.sendInGameScoreBoard(teamRespawners: TeamRespawners) {
        val displayName = "$PRIMARY${EXTRA}SERVERNAME$IMPORTANT$EXTRA.$PRIMARY${EXTRA}NET"
        val scores = mutableSetOf<Pair<String, Int>>().apply {
            teams.forEach { team ->
                val s = "${team.chatColor}${team.name}"
                val s1 = if (teamRespawners.hasRespawner(team)) "§a✔" else "§4✗"
                val s2 = if (team.players.isEmpty()) "$DARK_GRAY${stripColor(s1)}$GRAY$s" else "$s1$s"
                add(s2 to team.size)
            }
        }.toMap().toScoreboardScore().toSet()
        sendScoreBoard(displayName, scores)
    }

}

class TeamRespawners(private val respawners: Set<TeamRespawner>) {

    private val replacement: Material = Material.AIR

    private val GameTeam.respawner: TeamRespawner? get() = respawners.find { it.team == this }

    fun breakRespawner(player: Player, location: Location) {
        val playerTeam = player.team ?: return
        if (!location.isRespawner(playerTeam)) return
        val respawner: Respawner = getTeam(location)?.respawner ?: return
        if (respawner.clearAllOnBreak)
            respawner.respawners.forEach { it.toReplacement() }
        else location.toReplacement()
    }

    fun getTeam(location: Location): GameTeam? = teams.find { location.isRespawner(it) }

    fun hasRespawner(team: GameTeam): Boolean {
        val respawner: Respawner = team.respawner ?: return false
        return respawner.respawners.any { respawner.checkTypes(it) }
    }

    private fun Location.isRespawner(team: GameTeam): Boolean {
        val respawner: Respawner = team.respawner ?: return false
        return respawner.respawners.any { it.equalsInt(this) } && respawner.checkTypes(this)
    }

    private fun Respawner.checkTypes(location: Location): Boolean = types.any { location.block.type == it }
    private fun Location.toReplacement() {
        block.type = replacement
    }

    //"${Messages.PREFIX}${Colors.TEXT}Du kannst dein eigenes Bett nicht ${Colors.IMPORTANT}abbauen"
    //"${Messages.PREFIX}${Colors.TEXT}${playerTeam.chatColor}${player.displayName}${Colors.TEXT} hat das Bett von ${locationTeam.chatColor}${locationTeam.name}${Colors.TEXT} abgebaut"
}

interface Respawner {
    val respawners: Set<Location>
    val clearAllOnBreak: Boolean
    val types: Set<Material>
}

interface TeamRespawner : Respawner {
    val team: GameTeam
}

data class DataTeamRespawner(
    override val team: GameTeam,
    override val respawners: Set<Location>,
    override val clearAllOnBreak: Boolean = true,
    override val types: Set<Material> = setOf(Material.BED_BLOCK)
) : TeamRespawner

