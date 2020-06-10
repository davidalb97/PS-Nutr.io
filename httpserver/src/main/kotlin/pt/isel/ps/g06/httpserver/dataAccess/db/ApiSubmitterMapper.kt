package pt.isel.ps.g06.httpserver.dataAccess.db

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository

@Repository
data class ApiSubmitterMapper(
        private val submitterDbRepository: SubmitterDbRepository,
        val apiSubmitters: Map<Int, RestaurantApiType>
) {
    @Bean
    fun createMap(): Map<Int, RestaurantApiType> {
        val submitters = submitterDbRepository
                .getApiSubmitters()
                .associate(this::buildSubmitterPair)

        if (submitters.size != RestaurantApiType.values().size) {
            //TODO Better exception and log
            throw IllegalStateException()
        }

        return submitters
    }

    private fun buildSubmitterPair(submitter: DbSubmitterDto): Pair<Int, RestaurantApiType> {
        return Pair(submitter.submitter_id, RestaurantApiType.getOrDefault(submitter.submitter_name))
    }
}