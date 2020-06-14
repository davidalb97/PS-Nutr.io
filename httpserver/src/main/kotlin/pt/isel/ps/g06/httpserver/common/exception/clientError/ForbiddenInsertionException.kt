package pt.isel.ps.g06.httpserver.common.exception.clientError

open class ForbiddenInsertionException(val detail: String) : Exception(detail)