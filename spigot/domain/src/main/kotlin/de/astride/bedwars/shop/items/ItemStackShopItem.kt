/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.shop.items

import de.astride.bedwars.action.callAction
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.copy
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.count
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.removeItems
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 13:21.
 * Last edit 30.05.2019
 */
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