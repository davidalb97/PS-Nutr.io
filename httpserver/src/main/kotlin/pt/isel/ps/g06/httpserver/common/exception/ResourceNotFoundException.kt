package pt.isel.ps.g06.httpserver.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class ResourceNotFoundException(reason: String) : ResponseStatusException(HttpStatus.NOT_FOUND, reason)