package pt.isel.ps.g06.httpserver.common.exception.server

/**
 * Thrown whenever a required environment variable is missing from the host machine
 */
class SecretEnvVariableNotSetupException(
        title: String = "The server secret environment variable is not setup in the host machine"
) : Exception(title)