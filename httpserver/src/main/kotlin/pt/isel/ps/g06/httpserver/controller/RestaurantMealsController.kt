package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.dataAccess.RestaurantApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.DbRestaurantRepository

@RestController
@RequestMapping("/restaurant/{id}/meal")
class RestaurantMealsController(
        private val dbRestaurantRepo: DbRestaurantRepository,
        private val restaurantApiRepo: RestaurantApiRepository
) {

    @PostMapping(consumes = ["application/json"])
    fun addRestaurantMeal(@PathVariable id: String, @RequestBody meal: String) {

    }

    @PostMapping("/{mealId}/portion", consumes = ["application/json"])
    fun addMealPortion(@PathVariable id: String, @PathVariable mealId: String, @RequestBody portion: String) {

    }

    @PostMapping("/{mealId}/report", consumes = ["application/json"])
    fun addMealReport(@PathVariable id: String, @PathVariable mealId: String, @RequestBody report: String) {

    }

    @PostMapping("/{mealId}/vote", consumes = ["application/json"])
    fun addMealVote(@PathVariable id: String, @PathVariable mealId: String, @RequestBody vote: String) {

    }

    @PutMapping("/{mealId}/report", consumes = ["application/json"])
    fun updateMealReport(@PathVariable id: String, @PathVariable mealId: String, @RequestBody portion: String) {

    }

    @PutMapping("/{mealId}/vote", consumes = ["application/json"])
    fun updateMealVote(@PathVariable id: String, @PathVariable mealId: String, @RequestBody vote: String) {

    }

    @DeleteMapping("/{mealId}/portion", consumes = ["application/json"])
    fun deleteMealPortion(@PathVariable id: String, @PathVariable mealId: String, @RequestBody portion: String) {

    }

    @DeleteMapping("/{mealId}/vote", consumes = ["application/json"])
    fun deleteMealVote(@PathVariable id: String, @PathVariable mealId: String, @RequestBody vote: String) {

    }
}
