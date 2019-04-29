package de.astride.bedwars

import net.darkdevelopers.darkbedrock.darkness.general.functions.executeScripts
import net.darkdevelopers.darkbedrock.darkness.general.functions.performCraftPluginUpdater
import net.darkdevelopers.darkbedrock.darkness.general.functions.scriptEngineManager
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin
import org.bukkit.Bukkit
import javax.script.ScriptEngineManager

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.04.2019 05:47.
 * Current Version: 1.0 (23.04.2019 - 25.04.2019)
 */
@Suppress("unused") //Main class of the plugin
class BedWars : DarkPlugin() {

    override fun onLoad(): Unit = onLoad {
        performCraftPluginUpdater(
            mapOf(
                "type" to "DarkFrame-Spigot",
                "description" to description,
                "javaplugin" to this
            )
        )
    }

    override fun onEnable(): Unit = onEnable {

        var ns = System.nanoTime()

        scriptEngineManager = ScriptEngineManager(javaClass.classLoader)

        logger.info("Load scripts...")
        executeScripts(
            dataFolder.resolve("scripts"), mapOf(
                "type" to "BedWars-Spigot",
                "description" to description,
                "javaplugin" to this,
                "Bukkit" to Bukkit::class.java
            ),
            before = {
                ns = System.nanoTime()
                logger.info("Load script ${it.name}...")
            },
            after = { logger.info("Loaded script ${it.name} (in ${ns}ns)") }
        )
        logger.info("Loaded scripts")

    }

}