/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.action

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.04.2019 00:03.
 * Current Version: 1.0 (30.04.2019 - 30.04.2019)
 */
interface Action {

    val group: Any
    val code: (Map<String, Any?>) -> Unit

}