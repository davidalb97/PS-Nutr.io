package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.common.TransactionHolder
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository

@RestController
@RequestMapping("/restaurant/{id}/meal")
class RestaurantMealsController(
        private val transactionHolder: TransactionHolder,
        private val dbRestaurantRepo: RestaurantDbRepository,
        private val restaurantApiRepo: RestaurantApiRepository
) {

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantMeal(@PathVariable id: String, @RequestBody meal: String) {

    }

    @PostMapping("/{mealId}/portion", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addMealPortion(@PathVariable id: String, @PathVariable mealId: String, @RequestBody portion: String) {

    }

    @PostMapping("/{mealId}/report", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addMealReport(@PathVariable id: String, @PathVariable mealId: String, @RequestBody report: String) {

    }

    @PostMapping("/{mealId}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addMealVote(@PathVariable id: String, @PathVariable mealId: String, @RequestBody vote: String) {

    }

    @PutMapping("/{mealId}/report", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateMealReport(@PathVariable id: String, @PathVariable mealId: String, @RequestBody portion: String) {

    }

    @PutMapping("/{mealId}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateMealVote(@PathVariable id: String, @PathVariable mealId: String, @RequestBody vote: String) {

    }

    @DeleteMapping("/{mealId}/portion", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteMealPortion(@PathVariable id: String, @PathVariable mealId: String, @RequestBody portion: String) {

    }

    @DeleteMapping("/{mealId}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteMealVote(@PathVariable id: String, @PathVariable mealId: String, @RequestBody vote: String) {

    }
}
