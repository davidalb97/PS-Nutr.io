package pt.isel.ps.g06.httpserver.common.exception.clientError

import org.springframework.http.HttpStatus
import pt.isel.ps.g06.httpserver.common.exception.BaseResponseStatusException

class MissingInsulinProfileException(profileName: String) : BaseResponseStatusException(
        status = HttpStatus.NOT_FOUND,
        detail = "The specified user profile '$profileName' does not exist."
)