package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.VoteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.input.ReportInput
import pt.isel.ps.g06.httpserver.dataAccess.input.RestaurantInput
import pt.isel.ps.g06.httpserver.dataAccess.input.VoteInput
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantApiId

const val MAX_RADIUS = 1000

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
        private val dbRestaurantRepository: RestaurantDbRepository,
        private val dbReportRepository: ReportDbRepository,
        private val dbVoteRepository: VoteDbRepository,
        private val restaurantApiRepository: RestaurantApiRepository
) {

    @GetMapping
    fun getNearbyRestaurants(latitude: Float?, longitude: Float?, radius: Int? = MAX_RADIUS): String {
        return if (latitude == null || longitude == null) "emptySet()" else {
            val radius = if (radius != null && radius <= MAX_RADIUS) radius else MAX_RADIUS

            //Get from database and chosen API (if any)
            //Distinct those that aren't equal in both API and DB (how? By Db_api_id)
            //map to response and send it

            return ""
        }

//
//
//            CompletableFuture
//                    .supplyAsync { restaurantsRepository.getRestaurantsByCoordinates(latitude, longitude, radius) }
//                                .thenApply { it.map { dto -> mapToSiren(dto) } }
//                                .thenCombine(zomatoApi.searchRestaurants(latitude, longitude, radius)) { first, second ->
//                                    val list = second.restaurants.map { dto -> mapToSiren(dto.restaurant) }.toMutableList()
//                                    list.addAll(first)
//                        return list
//                    }
//        })
//    }
    }

    /**
     * Obtain more information for a Restaurant with given id.
     *
     * Current search algorithm will first query the Database for any restaurant, and only if none was found,
     * search the preferred Restaurant API (Zomato, etc.)
     */
    // TODO
    @GetMapping("/{id}", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getRestaurantInformation(@PathVariable id: Int, api: String?): String {
        return (dbRestaurantRepository.getById(id)
                ?: restaurantApiRepository
                        .getRestaurantApi(api!!)
                        .getRestaurantInfo(id)).toString()
    }

    // TODO
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRestaurant(@RequestBody restaurant: RestaurantInput) {
        dbRestaurantRepository.insert(
                restaurant.submitterId,
                restaurant.name,
                TODO(),
                emptyList(),
                restaurant.latitude,
                restaurant.longitude
        )
    }

    // TODO
    @DeleteMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRestaurant(@PathVariable id: Int) =
            dbRestaurantRepository.delete(1, id)

    // TODO
    @PostMapping("/{id}/report", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantReport(@PathVariable id: Int, @RequestBody report: ReportInput) =
            dbReportRepository.insert(
                    report.submitterId,
                    id,
                    report.description
            )

    // TODO
    @PostMapping("/{submission_id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantVote(@PathVariable submission_id: Int, @RequestBody vote: VoteInput) =
            dbVoteRepository.insert(vote.submitterId, submission_id, vote.value)

    // TODO
    @PutMapping("/{submission_id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRestaurantVote(@PathVariable submission_id: Int, @RequestBody vote: VoteInput) =
            dbVoteRepository.update(vote.submitterId, submission_id, vote.value)

    // TODO
    @DeleteMapping("/{submission_id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRestaurantVote(@PathVariable submission_id: Int, vote: VoteInput) =
            dbVoteRepository.delete(submission_id, vote.submitterId)
}