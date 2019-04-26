/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.functions

import org.bukkit.plugin.java.JavaPlugin
import javax.script.ScriptEngine

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 00:30.
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 26.04.2019 00:30.
 *
 * @throws RuntimeException if [ScriptEngine].get("javaplugin") is null or not a JavaPlugin
 *
 * Current Version: 1.0 (26.04.2019 - 26.04.2019)
 */
val ScriptEngine.javaPlugin: JavaPlugin
    get() = this["javaplugin"] as? JavaPlugin ?: throw RuntimeException("BreakingHandler needs a JavaPlugin")