package pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.exception

import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.zomato.ZomatoErrorDto

open class ZomatoException(val detail: ZomatoErrorDto, status: HttpStatus) : HttpStatusCodeException(status)

class ZomatoBadGatewayException(detail: ZomatoErrorDto) : ZomatoException(detail, HttpStatus.BAD_GATEWAY)

class ZomatoBadRequestException(detail: ZomatoErrorDto) : ZomatoException(detail, HttpStatus.BAD_REQUEST)