/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import com.google.gson.JsonObject
import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.services.ConfigService
import de.astride.bedwars.domain.services.configService
import de.astride.bedwars.domain.services.toConfigMap
import de.astride.bedwars.domain.services.unregisterConfigService
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import net.darkdevelopers.darkbedrock.darkness.spigot.configs.initSpigotStaticConfigMappings
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:24.
 * Last edit 01.06.2019
 */
object ConfigModule : Module {

    override var isRunning: Boolean = false
    private const val fileName = "config.json"

    private var configData: ConfigData? = null

    override fun setup(plugin: Plugin) {
        super.setup(plugin)

        initSpigotStaticConfigMappings()

        configData = ConfigData(plugin.dataFolder, fileName)
        val jsonObject = configData?.load<JsonObject>() ?: return
        configService = ConfigService(jsonObject.toMap())

    }

    override fun reset() {
        super.reset()

        val configData = configData
        if (configData != null) {
            GsonService.save(configData, configService.toConfigMap())
            this.configData = null
        }

        //initSpigotStaticConfigMappings() reset is not possible
        unregisterConfigService()

    }

}