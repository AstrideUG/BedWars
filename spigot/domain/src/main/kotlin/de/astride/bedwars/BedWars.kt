/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars

import de.astride.bedwars.modules.Module
import de.astride.bedwars.modules.instances.*
import net.darkdevelopers.darkbedrock.darkness.general.functions.performCraftPluginUpdater
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin


/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.04.2019 05:47.
 * Current Version: 1.0 (23.04.2019 - 30.05.2019)
 */
@Suppress("unused") //Main class of the plugin. Called from Bukkit plugin loader.
class BedWars : DarkPlugin() {

    private val moduleChain: Collection<Module> = setOf(
        ConfigModule,
        GameStatesModule,
        LogsModule,
        RespawnerModule,
        BreakingModule,
        ShopModule,
        ItemSpawnerModule
    )

    override fun onLoad(): Unit = onLoad {
        val map = mapOf(
            "type" to "BedWars-Spigot",
            "javaplugin" to this
        )
        performCraftPluginUpdater(map)
    }

    override fun onEnable(): Unit = onEnable {
        moduleChain.forEach { it.install() }
    }

    override fun onDisable(): Unit = onDisable {
        moduleChain.forEach { it.disable() }
    }

    private fun Module.install() {
        logger.info("Install $this...")
        setup(this@BedWars)
        logger.info("Installed $this")
    }

    private fun Module.disable() {
        logger.info("Install $this...")
        setup(this@BedWars)
        logger.info("Installed $this")
    }

}