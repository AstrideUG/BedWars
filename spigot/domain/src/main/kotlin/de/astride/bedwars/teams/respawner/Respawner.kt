/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.teams.respawner

import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultBlockLocation
import org.bukkit.Material

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:08.
 * Last edit 30.05.2019
 */
interface Respawner {
    val respawners: Set<DefaultBlockLocation>
    val clearAllOnBreak: Boolean
    val types: Set<Material>
}