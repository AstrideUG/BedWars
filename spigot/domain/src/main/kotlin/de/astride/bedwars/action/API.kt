/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.bedwars.action

/*
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.04.2019 00:05.
 * Current Version: 1.0 (30.04.2019 - 30.04.2019)
 */

private val actions: MutableMap<String, MutableCollection<Action>> = mutableMapOf()

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.04.2019 00:05.
 * Current Version: 1.0 (30.04.2019 - 30.04.2019)
 */
fun String.callAction(vars: Map<String, Any?>) {

    val separator = '.'
    val tokens = split(separator)
    var last = ""
    val all = tokens.map { last + separator + it.apply { last = this } }.mapNotNull { actions[this] }

    val map = vars + mapOf("id" to this)
    all.forEach { it.forEach { action -> action.code(map) } }

}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.04.2019 00:21.
 * Current Version: 1.0 (30.04.2019 - 30.04.2019)
 */
fun String.consume(group: Any, block: (Map<String, Any?>) -> Unit) {
    actions.getOrPut(this) { mutableListOf() } += DataAction(group, block)
}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.04.2019 00:42.
 *
 * if [group] is `null` all actions with is type will be removed
 *
 * Current Version: 1.0 (30.04.2019 - 30.04.2019)
 */
fun String.unregister(group: Any? = null) {
    if (group == null) actions -= this else actions[this]?.removeIf { it.group === group }
}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 30.04.2019 01:00.
 *
 * Removed all from this group
 *
 * Current Version: 1.0 (30.04.2019 - 30.04.2019)
 */
fun Any.unregister(): Unit = actions.forEach { (_, actions) -> actions.removeIf { it.group === this } }