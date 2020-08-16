package pt.isel.ps.g06.httpserver.model.food

import pt.isel.ps.g06.httpserver.model.NutritionalValues
import java.net.URI

open class Food(
        val identifier: Int,
        val name: String,
        val imageUri: URI? = null,
        val nutritionalValues: NutritionalValues
)