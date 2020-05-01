package pt.isel.ps.g06.httpserver.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class RestaurantNotFoundException(
        id: Int
) : ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant with id $id does not exist.")