/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules.instances

import de.astride.bedwars.domain.events.LobbyTemplate
import de.astride.bedwars.domain.modules.Module
import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:37.
 * Last edit 30.05.2019
 */
object GameStatesModule : Module {

    override fun setup(plugin: Plugin) {
        LobbyTemplate.setup(plugin)
    }

    override fun reset() {
        LobbyTemplate.reset()
    }

}