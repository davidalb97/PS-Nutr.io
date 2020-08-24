package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.INGREDIENTS
import pt.isel.ps.g06.httpserver.dataAccess.input.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.output.ingredient.IngredientsContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.ingredient.toIngredientsContainerOutput
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.IngredientService

@RestController
@RequestMapping(INGREDIENTS,
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE],
        consumes = [MediaType.ALL_VALUE]
)
class IngredientController(private val ingredientService: IngredientService) {

    /**
     * Obtain a list of ingredients.
     * @param   skip    Number of pages to be skipped
     * @param   limit   Page limit
     */
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

    /**
     * Create a hardcoded ingredient as moderator
     * @param   user    ModAuthorizationArgumentResolver's parameter
     * @param   ingredientInput    The ingredient to be inserted
     */
    @PostMapping
    fun createSuggestedIngredient(
            user: User,
            @RequestBody mealIngredientInput: MealInput
    ): ResponseEntity<IngredientsContainerOutput> {

        val createdIngredient = ingredientService.insertSuggestedIngredient(user.identifier, mealIngredientInput)

        return ResponseEntity
                .created(
                        UriComponentsBuilder
                                .fromUriString(INGREDIENTS)
                                .buildAndExpand(createdIngredient.identifier)
                                .toUri()
                )
                .build()
    }
}