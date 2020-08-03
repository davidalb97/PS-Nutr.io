package pt.isel.ps.g06.httpserver.springConfig.dto

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

class ServerConfigDto(var secret: String? = null)

