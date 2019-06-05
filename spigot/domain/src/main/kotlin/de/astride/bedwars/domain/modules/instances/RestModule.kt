/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import com.google.gson.JsonObject
import de.astride.bedwars.domain.modules.Module
import de.astride.bedwars.domain.services.ConfigService
import de.astride.bedwars.domain.services.configService
import net.darkdevelopers.darkbedrock.darkness.general.configs.toConfigMap
import net.darkdevelopers.darkbedrock.darkness.general.functions.format
import net.darkdevelopers.darkbedrock.darkness.general.functions.load
import net.darkdevelopers.darkbedrock.darkness.general.functions.toMap
import org.bukkit.plugin.Plugin
import spark.kotlin.Http
import spark.kotlin.halt
import spark.kotlin.ignite
import spark.kotlin.notFound

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 31.05.2019 17:31.
 * Last edit 05.06.2019
 */
object RestModule : Module {

    override val dependencies: Set<Module> = setOf(ConfigModule)
    override var isRunning: Boolean = false

    private var http: Http? = null

    override fun setup(plugin: Plugin) {
        super.setup(plugin)

        val http = ignite()
        http.port(10105)
        http.get("/") { "You are on the BedWars config REST-API" }
        http.service.path("/config") {
            http.before("/*") {
                val ips = configService.restApiAuthenticatedIps
                val authenticated: Boolean = ips.singleOrNull() == "*" || ips.any { it == request.ip() }
                if (!authenticated) {
                    plugin.logger.warning("[${javaClass.simpleName}] ${request.ip()} tried to call the rest-api (blocked)")
                    halt(401, """{"result":"no permissions"}""")
                }
                plugin.logger.info("[${javaClass.simpleName}] ${request.ip()} called the rest-api")
            }
            http.get("/show") {
                this.response.type("application/json")
                configService.toConfigMap().format()
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
        super.reset()
        http?.stop()
        http = null
    }

}
