/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain

import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.modules.disable
import de.astride.bedwars.domain.modules.install
import de.astride.bedwars.domain.modules.instances.*
import net.darkdevelopers.darkbedrock.darkness.general.functions.performCraftPluginUpdater
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin


/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.04.2019 05:47.
 * Current Version: 1.0 (23.04.2019 - 01.06.2019)
 */
@Suppress("unused") //Main class of the plugin. Called from Bukkit plugin loader.
class BedWars : DarkPlugin() {

    private val modules: Collection<Module> = setOf(
        ConfigModule,
        GameStatesModule,
        LogsModule,
        CommandsModule,
        RestModule
    )

    override fun onLoad(): Unit = onLoad {
        val map = mapOf(
            "type" to "BedWars-Spigot",
            "javaplugin" to this
        )
        performCraftPluginUpdater(map)
    }

    override fun onEnable(): Unit = onEnable {
        modules.install(this)
    }

    override fun onDisable(): Unit = onDisable {
        modules.disable(logger)
    }

}