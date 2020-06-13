package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper


/**
 * Responsible for mapping a dto from database or an API to a model object.
 *
 * @param T the Dto that will be mapped
 * @param R model object that is the result of mapping given dto
 */
interface ResponseMapper<T, R> {
    fun mapTo(dto: T): R
}

/**
 * Responsible for mapping a dto from database or an API to a model object.
 *
 * @param T the Dto that will be mapped
 * @param R model object that is the result of mapping given dto
 */
interface BiResponseMapper<T1, T2, R> {
    fun mapTo(dto: T1, userId: T2): R
}