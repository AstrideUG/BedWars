/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

import de.astride.bedwars.action.callAction
import de.astride.bedwars.action.consume
import de.astride.bedwars.functions.javaPlugin
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.inventory.InventoryBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.LeatherArmorItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.copy
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.isLeatherArmor
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.team
import net.darkdevelopers.darkbedrock.darkness.spigot.listener.Listener
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Colors
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Messages
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.count
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.fillGlass
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.line
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.removeItems
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryView
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import javax.script.ScriptEngine
import de.astride.bedwars.action.unregister as unregisterGroup

lateinit var listener: Listener

@Suppress("unused") //called by script loader
fun hooking(engine: ScriptEngine) {

    listener = AListener(engine.javaPlugin, mapOf()/*Map<ShopCategory, Collection<ShopItem>> = TODO ADD ConfigHandler*/)
    ActionHandler.register()

}

fun stop() {
    listener.unregister()
    ActionHandler.unregister()
}

class AListener(
    javaPlugin: JavaPlugin,
    private val categorized: Map<ShopCategory, Collection<ShopItem>>
) : Listener(javaPlugin) {

    private val shopName: String = "${Colors.SECONDARY}Shop"

    @EventHandler
    fun onPlayerInteractEntityEvent(event: PlayerInteractEntityEvent) {
        if (event.rightClicked.type != EntityType.VILLAGER) return
        event.cancel()
        event.player.openShop()
    }

    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) {

        val player = event.whoClicked as? Player ?: return
        val currentItem: ItemStack = event.currentItem ?: return
        val inventory: Inventory = event.whoClicked.openInventory?.topInventory ?: return
        if (inventory !== event.clickedInventory) return
        if (!inventory.title.equals(shopName, ignoreCase = true)) return

        event.cancel()

        val category = categorized.keys.find { it.display == currentItem }
        if (category != null) {
            inventory.fill(player, category)
            return
        }

        if (event.slot !in 27..53) return
        val item = categorized.values.flatten().find { it.display === currentItem } ?: return
        if (event.click.isShiftClick) item.buy(player, item.display.maxStackSize) else item.buy(player)

    }

    private fun Player.openShop(): InventoryView = this.openInventory(generateShop())

    private fun generateShop(): Inventory {
        val inventory = InventoryBuilder(45, shopName).build()
        categorized.keys.forEachIndexed { index, category -> inventory.setItem(index + line, category.display) }
        inventory.fillGlass(7)
        return inventory
    }

    private fun Inventory.fill(player: Player, category: ShopCategory) {

        val team = player.team ?: return
        val items = categorized.entries.find { it == category }?.value ?: return

        val itemStacks = items.map { it.display }.map { itemStack ->
            val type: Material = itemStack.type
            when {
                type === Material.STAINED_CLAY || type === Material.STAINED_GLASS ->
                    itemStack.copy(durability = getItemDurabilityID(team.name))
                type.isLeatherArmor() -> LeatherArmorItemBuilder(itemStack).setColor(team.leatherColor).build()
                else -> itemStack
            }
        }

        this.fillGlass(7) //add = 27
        this.sort(itemStacks) //add = 27

    }

    private fun Inventory.sort(itemStacks: List<ItemStack>) {
        val slot = 27
        when (itemStacks.size) {
            1 -> setItem(slot + 4, itemStacks[0])
            2 -> for (i in 0 until 2) setItem(slot + 2 + (4 * i), itemStacks[i])
            3 -> for (i in 2 until 6 step 2) setItem(slot + i, itemStacks[i / 2 - 1])
            4 -> for (i in 1 until 7 step 2) setItem(slot + i, itemStacks[(i - 1) / 2])
            5 -> for (i in 0 until 8 step 2) setItem(slot + i, itemStacks[i / 2])
            6 -> {
                for (i in 1..3) setItem(slot + i, itemStacks[i - 1])
                for (i in 5..7) setItem(slot + i, itemStacks[i - 2])
            }
            7 -> for (i in 0 until 7) setItem(slot + i + 1, itemStacks[i])
            8 -> {
                for (i in 0 until 3) setItem(slot + i, itemStacks[i])
                for (i in 5 until 8) setItem(slot + i, itemStacks[i - 1])
            }
            9 -> for (i in 0 until 8) setItem(slot + i, itemStacks[i])
        }
    }

    private fun getItemDurabilityID(name: String): Short = when (name) {
        "Weiß" -> 0
        "Orange" -> 1
        "Magenta" -> 2
        "Hellblau" -> 3
        "Gelb" -> 4
        "Hellgrün" -> 5
        "Rosa" -> 6
        "Grau" -> 7
        "Hellgrau" -> 8
        "Türkis" -> 9
        "Violettes" -> 10
        "Blau" -> 11
        "Braun" -> 12
        "Grün" -> 13
        "Rot" -> 14
        "Schwarz" -> 15
        else -> 8
    }

}

interface ShopCategory : Comparable<ShopCategory> {
    val display: ItemStack
    val items: Collection<ShopItem>
    val priority: Int get() = 0

    override fun compareTo(other: ShopCategory): Int = priority - other.priority
}

data class DataShopCategory(
    override val display: ItemStack,
    override val items: Collection<ShopItem>,
    override val priority: Int = 0
) : ShopCategory


interface ShopItem {

    val display: ItemStack
    fun buy(player: Player, maxTimes: Int = 1)

}

interface PriceShopItem : ShopItem {
    val price: Int
    val money: ItemStack
}

interface ItemStackShopItem : PriceShopItem {

    val items: Array<ItemStack>

    override fun buy(player: Player, maxTimes: Int) {

        if (maxTimes < 1) return

        val amount = player.inventory.count(money)
        if (amount < 0) return

        val rawCost = price * maxTimes
        val cost = if (rawCost > amount && maxTimes != 1) (amount / price.toDouble()).toInt() else rawCost

        val vars = mapOf(
            "maxTimes" to maxTimes,
            "amount" to amount,
            "cost" to cost,
            "display" to display,
            "price" to price,
            "money" to money,
            "items" to items,
            "player" to player
        )

        if (amount >= cost) {
            player.removeItems(money.copy(amount = cost))
            player.inventory.addItem(*items.map { it.copy(amount = it.amount * maxTimes) }.toTypedArray())
            "shop.item.buy.successfully".callAction(vars)
        } else "shop.item.buy.not.enough.money".callAction(vars)

    }

}

class DataItemStackShopItem(
    override val display: ItemStack,
    override val price: Int,
    override val money: ItemStack,
    override val items: Array<ItemStack>
) : ItemStackShopItem


object ActionHandler {

    fun register() {

        "shop.item.buy.successfully".consume(this) { vars ->

            val player = vars["player"] as? Player ?: return@consume
            player.playSound(player.location, Sound.ITEM_PICKUP, 1.0f, 1.0f)

        }

        "shop.item.buy.not.enough.money".consume(this) { vars ->

            val player = vars["player"] as? Player ?: return@consume
            val money = vars["money"] as? ItemStack ?: return@consume

            player.sendMessage("${Messages.PREFIX}${Colors.TEXT}Du besitzt nicht genug ${Colors.IMPORTANT}$money")
            player.playSound(player.location, Sound.NOTE_BASS_GUITAR, 1.0f, 1.0f)

        }

    }

    fun unregister(): Unit = this.unregisterGroup()

}
