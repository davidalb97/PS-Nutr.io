package pt.isel.ps.g06.httpserver.common.exception

class SecretEnvVariableNotSetupException(
        title: String = "The server secret environment variable is not setup in the host machine"
) : Exception(title)