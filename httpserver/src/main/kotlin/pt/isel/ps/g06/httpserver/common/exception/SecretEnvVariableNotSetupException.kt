package pt.isel.ps.g06.httpserver.common.exception

class SecretEnvVariableNotSetupException(
        detail: String = "The server secret environment variable is not setup in the host machine"
) : Exception(detail)