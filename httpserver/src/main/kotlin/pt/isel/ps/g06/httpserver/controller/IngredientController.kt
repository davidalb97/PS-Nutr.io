package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.INGREDIENTS
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
    ) {
        ingredientService.getIngredients(skip, limit)
    }
}