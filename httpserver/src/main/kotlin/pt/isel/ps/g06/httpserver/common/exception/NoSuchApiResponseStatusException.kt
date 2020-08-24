package pt.isel.ps.g06.httpserver.common.exception

import org.springframework.http.HttpStatus

class NoSuchApiResponseStatusException : BaseResponseStatusException(
        status = HttpStatus.BAD_REQUEST,
        title = "Could not find an API dependency for given submitter."
)