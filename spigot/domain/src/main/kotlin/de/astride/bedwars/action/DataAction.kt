/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.action

data class DataAction(override val group: Any, override val code: (Map<String, Any?>) -> Unit) : Action