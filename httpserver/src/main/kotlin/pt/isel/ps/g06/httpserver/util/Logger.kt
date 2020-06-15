
package pt.isel.ps.g06.httpserver.util

import org.slf4j.LoggerFactory
import pt.isel.ps.g06.httpserver.HttpServerApplication

private val logger = LoggerFactory.getLogger(HttpServerApplication::class.java)

fun log(e: Throwable) = logger.error(e.message, e)

fun log(s: String) = logger.debug(s)

