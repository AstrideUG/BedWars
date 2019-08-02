/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules

import org.bukkit.plugin.Plugin
import java.util.logging.Logger

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:21.
 * Last edit 01.06.2019
 */
interface Module {

    val dependencies: Set<Module> get() = emptySet()
    var isRunning: Boolean

    fun setup(plugin: Plugin) {
        isRunning = true
    }

    fun reset() {
        isRunning = false
    }

    @Suppress("unused")
    fun reload(plugin: Plugin) {
        reset()
        setup(plugin)
    }

}

fun Iterable<Module>.install(plugin: Plugin, logger: Logger = plugin.logger) {
    logger.info("Install modules (${joinToString { it.javaClass.simpleName }})...")
    forEach { it.install(plugin, logger) }
    logger.info("Installed modules")
}

fun Module.install(plugin: Plugin, logger: Logger = plugin.logger) {
    val name: String = this.javaClass.simpleName
    try {
        logger.info("Install $name...")
        dependencies.forEach { if (!it.isRunning) it.setup(plugin) }
        if (!isRunning) setup(plugin)
        logger.info("Installed $name")
    } catch (ex: Exception) {
        logger.warning("Thrown exception by installing module $name")
        ex.printStackTrace()
    }
}

fun Module.disable(logger: Logger) {
    val name: String = this.javaClass.simpleName
    try {
        logger.info("Disable $name...")
        if (isRunning) reset()
        logger.info("Disabled $name")
    } catch (ex: Exception) {
        logger.warning("Thrown exception by disabling module $name")
        ex.printStackTrace()
    }
}

fun Iterable<Module>.disable(logger: Logger) {
    logger.info("Disable modules (${joinToString { it.javaClass.simpleName }}...")
    sort().forEach { it.disable(logger) }
    logger.info("Disabled modules")
}

private fun Iterable<Module>.sort(result: MutableList<Module> = mutableListOf()): MutableList<Module> {
    forEach {
        if (it in result) return@forEach
        result += it
        it.dependencies.sort(result)
    }
    return result
}