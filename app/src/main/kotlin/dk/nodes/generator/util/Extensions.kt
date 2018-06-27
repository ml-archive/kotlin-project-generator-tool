package dk.nodes.generator.util

import java.io.File

fun File.endsWith(list: Array<String>): Boolean {

    for (item in list) {
        if (this.name.contains(item)) {
            println("endsWith: $name $item")
            return true
        }
    }
    return false
}