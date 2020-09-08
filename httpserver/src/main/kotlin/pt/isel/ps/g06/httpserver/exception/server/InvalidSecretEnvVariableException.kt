package pt.isel.ps.g06.httpserver.exception.server

import java.security.InvalidKeyException

/**
 * Thrown whenever a required environment variable is missing from the host machine
 */
class InvalidSecretEnvVariableException(
        exception: InvalidKeyException
) : Exception("The configured server secret environment variable is invalid!", exception)