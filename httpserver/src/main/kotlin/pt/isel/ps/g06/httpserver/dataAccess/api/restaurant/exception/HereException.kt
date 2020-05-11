package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.exception

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereErrorDto

open class HereException(val detail: HereErrorDto, status: HttpStatus) : HttpStatusCodeException(status)

class HereBadGatewayException(detail: HereErrorDto) : HereException(detail, HttpStatus.BAD_GATEWAY)

class HereBadRequestException(detail: HereErrorDto) : HereException(detail, HttpStatus.BAD_REQUEST)