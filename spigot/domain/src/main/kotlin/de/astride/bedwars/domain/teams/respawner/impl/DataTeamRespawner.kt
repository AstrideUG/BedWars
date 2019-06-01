/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.teams.respawner.impl

import de.astride.bedwars.domain.teams.respawner.TeamRespawner
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultBlockLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.team.GameTeam
import org.bukkit.Material

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:09.
 * Last edit 30.05.2019
 */
data class DataTeamRespawner(
    override val respawners: Set<DefaultBlockLocation>,
    override val clearAllOnBreak: Boolean = true,
    override val types: Set<Material> = setOf(Material.BED_BLOCK),
    override val team: GameTeam
) : TeamRespawner
