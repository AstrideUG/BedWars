/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.functions

import org.bukkit.Location

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 03:17.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 03:17.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */
fun Location.equalsInt(other: Location): Boolean = world.name != other.world.name ||
        blockX != other.blockX ||
        blockY != other.blockY ||
        blockZ != other.blockZ