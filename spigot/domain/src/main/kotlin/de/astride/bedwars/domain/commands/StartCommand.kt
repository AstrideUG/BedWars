/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.commands

import de.astride.bedwars.domain.events.LobbyTemplate
import de.astride.bedwars.domain.functions.javaPlugin
import de.astride.bedwars.domain.services.configService
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendTo
import org.bukkit.command.CommandSender

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 07.06.2019 02:59.
 * Last edit 07.06.2019
 */
class StartCommand : Command(
    javaPlugin,
    "Start",
    "bedwars.commands.start",
    usage = "[seconds]",
    maxLength = 1
) {

    override fun perform(sender: CommandSender, args: Array<String>) {
        val countdown = LobbyTemplate.countdown
        countdown.seconds = args.getOrNull(0)?.toIntOrNull() ?: 10
        configService.commandStartSuccessesMessage.replace("@seconds@", countdown.seconds.toString()).sendTo(sender)
    }

}