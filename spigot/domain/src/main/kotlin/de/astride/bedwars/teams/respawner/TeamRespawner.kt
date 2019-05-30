/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.teams.respawner

import net.darkdevelopers.darkbedrock.darkness.spigot.team.GameTeam

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.05.2019 12:08.
 * Last edit 30.05.2019
 */
interface TeamRespawner : Respawner {
    val team: GameTeam
}