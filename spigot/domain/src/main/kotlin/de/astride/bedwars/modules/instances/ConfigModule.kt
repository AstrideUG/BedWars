/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.modules.instances

import com.google.gson.JsonObject
import de.astride.bedwars.modules.Module
import de.astride.bedwars.services.ConfigService
import de.astride.bedwars.services.configService
import de.astride.bedwars.services.unregisterConfigService
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.initSpigotStaticConfigMappings
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:24.
 * Last edit 30.05.2019
 */
object ConfigModule : Module {

    private const val fileName = "config.json"

    override fun setup(plugin: Plugin) {

        initSpigotStaticConfigMappings()

        val configData = ConfigData(plugin.dataFolder, fileName)
        val jsonObject = configData.load<JsonObject>()
        configService = ConfigService(jsonObject.toMap())

    }

    override fun reset() {
        //initSpigotStaticConfigMappings() reset is not possible
        unregisterConfigService()
    }

}