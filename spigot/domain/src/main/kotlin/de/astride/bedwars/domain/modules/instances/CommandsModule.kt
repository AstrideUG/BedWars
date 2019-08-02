/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.commands.StartCommand
import de.astride.bedwars.domain.modules.Module
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 07.06.2019 03:06.
 * Last edit 07.06.2019
 */
object CommandsModule : Module {

    override var isRunning: Boolean = false
    override val dependencies: Set<Module> = setOf(ConfigModule)
    private val commands = mutableSetOf<Command>()

    override fun setup(plugin: Plugin) {
        super.setup(plugin)
        commands += StartCommand()
    }

    //todo reset commands

}