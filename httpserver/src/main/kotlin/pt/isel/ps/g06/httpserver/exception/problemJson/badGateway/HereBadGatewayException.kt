package pt.isel.ps.g06.httpserver.exception.problemJson.badGateway

import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.dto.HereErrorDto
import pt.isel.ps.g06.httpserver.util.log

class HereBadGatewayException(error: HereErrorDto) : BaseBadGatewayException(
        title = "Our restaurant providers are currently offline, please try again later",
        detail = error.cause
) {
    init {
        log("Failed to obtain HERE restaurants - Status code is ${error.status} with cause '${error.cause}'")
    }
}