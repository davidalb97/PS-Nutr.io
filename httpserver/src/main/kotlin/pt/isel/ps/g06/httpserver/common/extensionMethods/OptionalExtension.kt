package pt.isel.ps.g06.httpserver.common.extensionMethods

import java.util.*

fun <T> Optional<T>.toNullable(): T? = orElse(null)
