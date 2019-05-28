package de.astride.bedwars

import com.google.gson.JsonObject
import de.astride.bedwars.events.LobbyTemplate
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService.loadAs
import net.darkdevelopers.darkbedrock.darkness.general.functions.*
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin
import java.io.File
import javax.script.ScriptEngineManager

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.04.2019 05:47.
 * Current Version: 1.0 (23.04.2019 - 25.04.2019)
 */
@Suppress("unused") //Main class of the plugin
class BedWars : DarkPlugin() {

    override fun onLoad(): Unit = onLoad {
        val map = mapOf(
            "type" to "BedWars-Spigot",
            "javaplugin" to this
        )
        performCraftPluginUpdater(map)
    }

    override fun onEnable(): Unit = onEnable {

        scripts()

        LobbyTemplate.setup(this)

    }

    private fun scripts() {
        var ns = System.nanoTime()

        scriptEngineManager = ScriptEngineManager(javaClass.classLoader)

        logger.info("Load scripts...")

        val configData = ConfigData(dataFolder, "cashed-scripts.json")
        val jsonObject = loadAs(configData) ?: JsonObject()
        @Suppress("UNCHECKED_CAST")
        val hashs = jsonObject.entrySet()
            .map { it.key to it.value.asString() }
            .mapNotNull { if (it.second == null) null else it }
            .toMap() as Map<String, String>

        val newHashs = mutableMapOf<String, String>()

        executeScripts(
            dataFolder.resolve("scripts"), mapOf(
                "type" to "BedWars-Spigot",
                "javaplugin" to this
            ),
            before = { file: File, _: String, _: String ->
                ns = System.nanoTime()
                logger.info("Load script ${file.name}...")
            },
            after = { file: File, input: String, hash: String ->
                newHashs[file.name] = input.sha256()
                logger.info("Loaded script ${file.name} (in ${ns}ns)")
            },
            hashs = hashs
        )
        logger.info("Loaded scripts")
    }

}