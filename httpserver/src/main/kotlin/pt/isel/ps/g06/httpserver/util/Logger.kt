package pt.isel.ps.g06.httpserver.util

import com.sun.org.slf4j.internal.LoggerFactory
import pt.isel.ps.g06.httpserver.HttpServerApplication

private val logger = LoggerFactory.getLogger(HttpServerApplication::class.java)

fun log(e: Exception) = logger.error(e.message, e)

fun log(s: String) = logger.debug(s)