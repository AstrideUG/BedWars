/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.domain.functions

import de.astride.bedwars.domain.BedWars
import org.bukkit.plugin.java.JavaPlugin

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 29.05.2019 15:41.
 * Last edit 29.05.2019
 */

val javaPlugin: JavaPlugin get() = BedWars::class.java.instance!!

private val <P : JavaPlugin> Class<P>.instance: JavaPlugin? get() = JavaPlugin.getPlugin(this)