package pt.isel.ps.g06.httpserver.exception.problemJson.notFound

class MealNotFoundException(title: String = "Given meal was not found.") : BaseNotFoundException(
        title = title
)