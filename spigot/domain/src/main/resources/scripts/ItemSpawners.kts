/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 25.04.2019 23:56.
 * Current Version: 1.0 (25.04.2019 - 26.04.2019)
 */

import de.astride.bedwars.functions.javaPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.ItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Colors
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import javax.script.ScriptEngine

lateinit var itemSpawners: ItemSpawners

@Suppress("unused") //called by script loader
fun hooking(engine: ScriptEngine) {
    itemSpawners = ItemSpawners(engine.javaPlugin)
}

fun stop() = itemSpawners.stop()

interface ItemSpawner {

    val drop: ItemStack
    val location: Set<Location>
    val allowed: (Int) -> Boolean

}

data class DataItemSpawner(
    override val drop: ItemStack,
    override val location: Set<Location>,
    override val allowed: (Int) -> Boolean
) : ItemSpawner

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 25.04.2019 23:58.
 * Current Version: 1.0 (25.04.2019 - 26.04.2019)
 */
class ItemSpawners(javaPlugin: JavaPlugin) {

    private val spawners: Set<ItemSpawner> = setOf(
        DataItemSpawner(
            ItemBuilder(Material.CLAY_BRICK, 2).setName("${Colors.IMPORTANT}Bronze").build(),
            setOf()
        ) { true },
        DataItemSpawner(
            ItemBuilder(Material.IRON_INGOT).setName("${Colors.IMPORTANT}Eisen").build(),
            setOf()
        ) { it == round / 5 || it == round / 3 || it == round / 1 },
        DataItemSpawner(
            ItemBuilder(Material.GOLD_INGOT).setName("${Colors.IMPORTANT}Gold").build(),
            setOf()
        ) { it == round }
    )
    private val task: Int = Bukkit.getScheduler().scheduleSyncRepeatingTask(javaPlugin, {
        spawners.forEach { spawner ->
            if (spawner.allowed(timer)) spawner.location.forEach { it.world.dropItem(it, spawner.drop) }
        }
        timer++
        if (timer >= round) timer = 0
    }, 0, 20)
    private var round: Int = 0
    private var timer: Int = 0

    //TODO: Locations data serializer

    init {
        calcRound()
    }

    fun stop() = Bukkit.getScheduler().cancelTask(task)

    private fun calcRound(players: Int = de.astride.bedwars.players.size) {
        this.round = when {
            players <= 4 -> 60
            players <= 8 -> 40
            players <= 12 -> 20
            else -> 10
        }
    }

}
