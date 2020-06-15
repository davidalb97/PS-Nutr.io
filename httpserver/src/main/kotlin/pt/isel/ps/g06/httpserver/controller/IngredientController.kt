package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.INGREDIENTS
import pt.isel.ps.g06.httpserver.dataAccess.output.ingredient.IngredientsContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.ingredient.toIngredientsContainerOutput
import pt.isel.ps.g06.httpserver.service.IngredientService

@RestController
@RequestMapping(INGREDIENTS,
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE],
        consumes = [MediaType.ALL_VALUE]
)
class IngredientController(private val ingredientService: IngredientService) {

    @GetMapping
    fun getIngredients(
            @RequestParam skip: Int?,
            @RequestParam limit: Int?
    ): ResponseEntity<IngredientsContainerOutput> {
        val ingredients = ingredientService.getIngredients(skip, limit)

        return ResponseEntity
                .ok()
                .body(toIngredientsContainerOutput(ingredients.toList()))
    }
}