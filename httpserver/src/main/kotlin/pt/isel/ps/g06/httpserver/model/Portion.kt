package pt.isel.ps.g06.httpserver.model

class Portion(
        val amount: Int,
        val unit: String
) {
    companion object {
        const val TODO_DEFAULT_UNIT = "grams"
    }
}