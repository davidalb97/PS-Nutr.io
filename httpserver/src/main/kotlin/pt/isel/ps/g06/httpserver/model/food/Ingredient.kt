package pt.isel.ps.g06.httpserver.model.food

import pt.isel.ps.g06.httpserver.model.NutritionalValues
import java.net.URI

class Ingredient(
        identifier: Int,
        name: String,
        imageUri: URI? = null,
        nutritionalValues: NutritionalValues
) : Food(
        identifier = identifier,
        name = name,
        imageUri = imageUri,
        nutritionalValues = nutritionalValues
)