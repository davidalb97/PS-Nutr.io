package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.data.ReportInput
import pt.isel.ps.g06.httpserver.data.RestaurantInput
import pt.isel.ps.g06.httpserver.data.VoteInput
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbReportRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbRestaurantRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbVotableRepository

const val MAX_RADIUS = 1000

@RestController
@RequestMapping("/restaurant")
class RestaurantController(
        private val dbRestaurantRepository: DbRestaurantRepository,
        private val dbReportRepository: DbReportRepository,
        private val dbVotableRepository: DbVotableRepository,
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
        return (dbRestaurantRepository.getRestaurantById(id)
                ?: restaurantApiRepository
                        .getRestaurantApi(api!!)
                        .getRestaurantInfo(id)).toString()
                ?: return "The restaurant does not exist."
    }

    // TODO
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createRestaurant(@RequestBody restaurant: RestaurantInput) {
        dbRestaurantRepository.insertRestaurant(
                restaurant.submitterId,
                restaurant.name,
                null,
                emptyList(),
                restaurant.latitude,
                restaurant.longitude,
                null
        )
    }

    // TODO
    @DeleteMapping("/{id}", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRestaurant(@PathVariable id: Int) =
            dbRestaurantRepository.deleteRestaurant(1, id)

    // TODO
    @PostMapping("/{id}/report", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantReport(@PathVariable id: Int, @RequestBody report: ReportInput) =
            dbReportRepository.addReport(
                    report.submitterId,
                    id,
                    report.description
            )

    // TODO
    @PostMapping("/{submission_id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun addRestaurantVote(@PathVariable submission_id: Int, @RequestBody vote: VoteInput) =
            dbVotableRepository.addVote(vote.submitterId, submission_id, vote.value)

    // TODO
    @PutMapping("/{submission_id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun updateRestaurantVote(@PathVariable submission_id: Int, @RequestBody vote: VoteInput) =
            dbVotableRepository.updateVote(vote.submitterId, submission_id, vote.value)

    // TODO
    @DeleteMapping("/{submission_id}/vote", consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun deleteRestaurantVote(@PathVariable submission_id: Int, vote: VoteInput) =
            dbVotableRepository.removeVote(submission_id, vote.submitterId)
}