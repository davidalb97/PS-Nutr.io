package pt.isel.ps.g06.httpserver.exception.problemJson.notFound

class PortionNotFoundException(title: String = "Given restaurant meal portion was not found.") : BaseNotFoundException(
        title = title
)