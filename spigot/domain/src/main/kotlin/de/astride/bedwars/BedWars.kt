package de.astride.bedwars

import net.darkdevelopers.darkbedrock.darkness.general.functions.executeScripts
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.04.2019 05:47.
 * Current Version: 1.0 (23.04.2019 - 23.04.2019)
 */
class BedWars : DarkPlugin() {

    override fun onEnable(): Unit = onEnable {

        var ns = System.nanoTime()

        logger.info("Load scripts...")
        executeScripts(
            dataFolder.resolve("scripts"), mapOf(
                "type" to "BedWars-Spigot",
                "description" to description,
                "javaplugin" to this
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