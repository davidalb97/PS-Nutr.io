package pt.isel.ps.g06.httpserver.dataAccess.output.meal

import java.net.URI

class CustomMealOutput(
        val name: String,
        val carbs: Int,
        val quantity: Int,
        val unit: String,
        val ingredients: Iterable<String>,
        val meals: Iterable<String>,
        val cuisines: Iterable<String>,
        val imageUri: URI?
)