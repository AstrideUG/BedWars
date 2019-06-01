/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import com.google.gson.JsonObject
import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.services.ConfigService
import de.astride.bedwars.domain.services.configService
import de.astride.bedwars.domain.services.toMap
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import org.bukkit.plugin.Plugin
import spark.kotlin.Http
import spark.kotlin.ignite
import spark.kotlin.notFound

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 31.05.2019 17:31.
 * Last edit 31.05.2019
 */
object RestModule : Module {

    private var http: Http? = null

    override fun setup(plugin: Plugin) {
        val http = ignite()
        http.port(10105)
        http.get("/") { "You are on the BedWars config REST-API" }
        http.service.path("/config") {
            http.get("/show") {
                response.type("application/json")
                GsonService.formatJson(configService.toMap())
            }
            http.post("/reload", "application/json") {
                val values = request.body().load<JsonObject>().toMap()
                configService = ConfigService(values)
                plugin.logger.info("Reloaded config from ${request.ip()}")
                """{"result":"Reloaded"}"""
            }
        }
        notFound { """{"error":"404 Not Found"}""" }
    }

    override fun reset() {
        http?.stop()
        http = null
    }

}
