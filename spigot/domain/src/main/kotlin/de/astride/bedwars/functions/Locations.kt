/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.functions

import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toBukkitWorld
import net.darkdevelopers.darkbedrock.darkness.spigot.location.extensions.BukkitLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.extensions.toLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.Location
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.DefaultBlockLocation
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.x
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.y
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.alliases.z
import net.darkdevelopers.darkbedrock.darkness.spigot.location.location.inmutable.extensions.locationOf
import net.darkdevelopers.darkbedrock.darkness.spigot.location.vector.inmutable.Vector3
import net.darkdevelopers.darkbedrock.darkness.spigot.location.vector.inmutable.extensions.alliases.Vector3D
import net.darkdevelopers.darkbedrock.darkness.spigot.location.vector.inmutable.extensions.alliases.Vector3I
import net.darkdevelopers.darkbedrock.darkness.spigot.location.vector.inmutable.extensions.vector3Of

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
fun BukkitLocation.equalsInt(other: BukkitLocation): Boolean = world.name != other.world.name ||
        blockX != other.blockX ||
        blockY != other.blockY ||
        blockZ != other.blockZ

//TODO add all below to darkness
fun <W, OV : Vector3I> Location<W, OV>.toLocation3D(): Location<W, Vector3D> =
    map(mappedVector = { this.map({ toDouble() }) })

fun <W, OV : Vector3D> Location<W, OV>.toLocation3I(): Location<W, Vector3I> =
    map(mappedVector = { this.map({ toInt() }) })

fun <OW, NW, V : Vector3<*>> Location<OW, V>.map(
    mappedWorld: OW.() -> NW,
    newWorld: NW = world.mappedWorld()
): Location<NW, V> =
    map(mappedWorld, { this }, newWorld)

fun <W, OV : Vector3<*>, NV : Vector3<*>> Location<W, OV>.map(
    mappedVector: OV.() -> NV,
    newVector: NV = vector.mappedVector()
): Location<W, NV> =
    map({ this }, mappedVector, newVector = newVector)

fun <OW, NW, OV : Vector3<*>, NV : Vector3<*>> Location<OW, OV>.map(
    mappedWorld: OW.() -> NW,
    mappedVector: OV.() -> NV,
    newWorld: NW = world.mappedWorld(),
    newVector: NV = vector.mappedVector()
): Location<NW, NV> = locationOf(newWorld, newVector)

fun <O, N> Vector3<O>.map(
    mapped: O.() -> N,
    newX: N = x.mapped(),
    newY: N = y.mapped(),
    newZ: N = z.mapped()
): Vector3<N> = vector3Of(newX, newY, newZ)

fun DefaultBlockLocation.toBukkitLocation(): BukkitLocation = BukkitLocation(
    world.toBukkitWorld(), x.toDouble(), y.toDouble(), z.toDouble()
)

fun BukkitLocation.toDefaultBlockLocation(): DefaultBlockLocation = toLocation().toLocation3I()
