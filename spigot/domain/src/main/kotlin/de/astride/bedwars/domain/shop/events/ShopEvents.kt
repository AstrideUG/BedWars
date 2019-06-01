/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.shop.events

import de.astride.bedwars.domain.services.configService
import de.astride.bedwars.domain.shop.categories.ShopCategory
import de.astride.bedwars.domain.shop.items.ShopItem
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.inventory.InventoryBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.builder.item.LeatherArmorItemBuilder
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.copy
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.cancel
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.isLeatherArmor
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.listenTop
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.team
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import net.darkdevelopers.darkbedrock.darkness.spigot.messages.Colors
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.fillGlass
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.line
import org.bukkit.ChatColor
import org.bukkit.ChatColor.*
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:23.
 * Last edit 30.05.2019
 */
class ShopEvents(
    private val categorized: Map<ShopCategory, Iterable<ShopItem>> = configService.shopCategorizedItems
) : EventsTemplate() {

    private val shopName: String = "${Colors.SECONDARY}Shop"

    fun setup(plugin: Plugin) {
        listen<PlayerInteractEntityEvent>(plugin) { event ->
            if (event.rightClicked.type != EntityType.VILLAGER) return@listen
            event.cancel()
            event.player.openInventory(generateShop())
        }.add()
        shopName.listenTop(
            plugin,
            cancel = true,
            acceptWhoClicked = { it is Player },
            acceptSlot = { it in 27..53 }
        ) { event ->
            val player = event.whoClicked as Player
            val item = categorized.values.flatten().find { it.display === event.currentItem } ?: return@listenTop
            if (event.click.isShiftClick) item.buy(player, item.display.maxStackSize) else item.buy(player)
        }.add()
        shopName.listenTop(
            plugin,
            cancel = true,
            acceptWhoClicked = { it is Player },
            acceptSlot = { it < 27 }
        ) { event ->
            val player = event.whoClicked as Player
            val category = categorized.keys.find { it.display === event.currentItem } ?: return@listenTop
            event.clickedInventory.fill(player, category)
        }.add()
    }

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
                    itemStack.copy(durability = team.chatColor.toItemDurability())
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

    private fun ChatColor.toItemDurability(default: Short = 8): Short = when (this) {
        WHITE -> 0
        GOLD -> 1
        LIGHT_PURPLE -> 2
        BLUE -> 3
        YELLOW -> 4
        GREEN -> 5
        DARK_GRAY -> 7
        GRAY -> 8
        AQUA -> 9
        DARK_PURPLE -> 10
        DARK_BLUE -> 11
        DARK_GREEN -> 13
        RED -> 14
        BLACK -> 15
        else -> default
    }

}