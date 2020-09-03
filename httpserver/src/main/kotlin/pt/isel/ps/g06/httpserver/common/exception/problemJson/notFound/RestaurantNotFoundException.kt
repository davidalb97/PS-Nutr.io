package pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound

class RestaurantNotFoundException(title: String = "Given restaurant was not found.") : BaseNotFoundException(
        title = title
)