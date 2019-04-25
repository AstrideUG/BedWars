import net.darkdevelopers.darkbedrock.darkness.spigot.functions.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.java.JavaPlugin
import javax.script.ScriptEngine

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 25.04.2019 08:57.
 * Current Version: 1.0 (25.04.2019 - 25.04.2019)
 */
fun hooking(engine: ScriptEngine) {

    val javaPlugin = engine["javaplugin"] as? JavaPlugin ?: throw RuntimeException("BreakingHandler needs a JavaPlugin")
    BreakingListener(javaPlugin)

}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 25.04.2019 08:57.
 * Current Version: 1.0 (25.04.2019 - 25.04.2019)
 */
class BreakingListener(javaPlugin: JavaPlugin) : Listener(javaPlugin) {

    private val locations = mutableListOf<Location>()
    private val whitelisted = listOf(Material.DOUBLE_PLANT, Material.LONG_GRASS)

    @EventHandler
    fun onBlockPlaceEvent(event: BlockPlaceEvent) {
        locations += event.block.location
    }

    @EventHandler
    fun onBlockBreakEvent(event: BlockBreakEvent) {
        if (!event.block.isBreakable()) event.cancel()
        if (!event.isCancelled) locations -= event.block.location
    }

    private fun Block.isBreakable(): Boolean = if (whitelisted.any { it == type }) true else location.isIn()
    private fun Location.isIn(): Boolean = locations.any { it.equalsInt(block.location) }

}

fun Location.equalsInt(other: Location): Boolean = world.name != other.world.name ||
        blockX != other.blockX ||
        blockY != other.blockY ||
        blockZ != other.blockZ
