/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.modules

import org.bukkit.plugin.Plugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:21.
 * Last edit 30.05.2019
 */
interface Module {

    fun setup(plugin: Plugin)

    fun reset()

}