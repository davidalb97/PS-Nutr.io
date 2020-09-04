package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.INGREDIENTS_PATH
import pt.isel.ps.g06.httpserver.common.MAX_COUNT
import pt.isel.ps.g06.httpserver.common.exception.problemJson.badRequest.InvalidInputException
import pt.isel.ps.g06.httpserver.dataAccess.input.meal.MealInput
import pt.isel.ps.g06.httpserver.dataAccess.output.ingredient.IngredientsContainerOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.ingredient.toIngredientsContainerOutput
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.IngredientService
import pt.isel.ps.g06.httpserver.service.UserService
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
@RestController
@RequestMapping(INGREDIENTS_PATH,
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE],
        consumes = [MediaType.ALL_VALUE]
)
class IngredientController(
        private val ingredientService: IngredientService,
        private val userService: UserService
) {

    /**
     * Obtain a list of ingredients.
     * @param   skip    Number of pages to be skipped
     * @param   limit   Page limit
     * @return  [ResponseEntity]<[IngredientsContainerOutput]>
     */
    @GetMapping
    fun getIngredients(
            @RequestParam @Min(0) skip: Int?,
            @RequestParam @Min(0) @Max(MAX_COUNT) count: Int?
    ): ResponseEntity<IngredientsContainerOutput> {

        val ingredients = ingredientService.getIngredients(skip, count)

        return ResponseEntity
                .ok()
                .body(toIngredientsContainerOutput(ingredients.toList()))
    }

    /**
     * Create a hardcoded ingredient as moderator
     * @param   user    ModAuthorizationArgumentResolver's parameter
     * @return  [ResponseEntity]<[IngredientsContainerOutput]>
     * @throws InvalidInputException On invalid cuisines passed
     */
    @PostMapping
    fun createSuggestedIngredient(
            @RequestBody mealIngredientInput: MealInput,
            user: User
    ): ResponseEntity<IngredientsContainerOutput> {

        // Check if the user is a moderator
        userService.ensureModerator(user)

        val createdIngredient = ingredientService.insertSuggestedIngredient(user.identifier, mealIngredientInput)

        return ResponseEntity
                .created(
                        UriComponentsBuilder
                                .fromUriString(INGREDIENTS_PATH)
                                .buildAndExpand(createdIngredient.identifier)
                                .toUri()
                )
                .build()
    }
}