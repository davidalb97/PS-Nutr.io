package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.dataAccess.database.repos.RestaurantsRepository

@RestController
@RequestMapping("/restaurant")
class RestaurantController(private val restaurantsRepository: RestaurantsRepository) {

    @GetMapping()
    fun restaurantsHandler(latitude: Float, longitude: Float) {
        val result = restaurantsRepository.getRestaurantsByCoordinates(latitude, longitude)
        println(result.isEmpty())
    }
}