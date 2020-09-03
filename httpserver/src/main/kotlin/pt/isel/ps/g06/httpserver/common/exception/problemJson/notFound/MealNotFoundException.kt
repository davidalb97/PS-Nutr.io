package pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound

class MealNotFoundException(title: String = "Given meal was not found.") : BaseNotFoundException(
        title = title
)