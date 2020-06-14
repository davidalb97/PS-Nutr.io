package pt.isel.ps.g06.httpserver.common.exception.notFound

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

open class ResourceNotFoundException(detail: String) : BaseResponseStatusException(
        status = HttpStatus.NOT_FOUND,
        detail = detail
)