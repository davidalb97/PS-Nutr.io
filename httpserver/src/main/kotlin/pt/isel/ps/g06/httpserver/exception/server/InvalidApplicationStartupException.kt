package pt.isel.ps.g06.httpserver.exception.server

/**
 * Thrown whenever server startup logic fails, and thus stopping the
 * HTTP Server from ever loading.
 */
class InvalidApplicationStartupException(message: String) : Exception(message)