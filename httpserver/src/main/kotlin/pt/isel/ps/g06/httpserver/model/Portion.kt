package pt.isel.ps.g06.httpserver.model

class Portion(
        val amount: Int,
        val unit: String
) {
    //TODO do not use this, get from db!
    companion object {
        const val TODO_DEFAULT_UNIT = "grams"
    }
}