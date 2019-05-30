/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.itemspawner

import de.astride.bedwars.functions.javaPlugin
import de.astride.bedwars.services.configService
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitScheduler

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 25.04.2019 23:58.
 * Current Version: 1.0 (25.04.2019 - 30.05.2019)
 */

@Suppress("EXPERIMENTAL_UNSIGNED_LITERALS", "EXPERIMENTAL_API_USAGE")
class ItemSpawners(
    private val plugin: Plugin = javaPlugin,
    private val spawners: Set<ItemSpawner> = configService.itemSpawnersSpawners,
    private val round: UInt = configService.itemSpawnersRound
) {

    private val scheduler: BukkitScheduler get() = plugin.server.scheduler

    private var timer: UInt = 0u
    private val task: Int = scheduler.scheduleSyncRepeatingTask(plugin, {
        spawners.forEach { spawner ->
            if (spawner.allowed(timer)) spawner.location.forEach { it.world.dropItem(it, spawner.drop) }
        }
        timer++
        if (timer >= round) timer = 0u
    }, 0, configService.itemSpawnersDelay)

    fun stop(): Unit = scheduler.cancelTask(task)

}
