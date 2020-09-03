package pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound

class RestaurantMealNotFound(title: String = "Given restaurant meal was not found.") : BaseNotFoundException(
        title = title
)