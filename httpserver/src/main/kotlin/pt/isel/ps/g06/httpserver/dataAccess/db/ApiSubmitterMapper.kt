package pt.isel.ps.g06.httpserver.dataAccess.db

import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository

@Repository
data class ApiSubmitterMapper(
        private val submitterDbRepository: SubmitterDbRepository,
        private val apiSubmitters: Map<Int, RestaurantApiType>
) {
    @Bean
    protected fun createMap(): Map<Int, RestaurantApiType> {
        val submitters = submitterDbRepository
                .getApiSubmittersByName(RestaurantApiType.values().map { it.toString() })
                .associate(this::buildSubmitterPair)

        if (submitters.size != RestaurantApiType.values().size) {
            //TODO Better exception and log
            throw IllegalStateException()
        }

        return submitters
    }

    fun getApiType(id: Int): RestaurantApiType? = apiSubmitters[id]

    fun getSubmitter(type: RestaurantApiType) = apiSubmitters.filterValues { it == type }.keys.firstOrNull()

    private fun buildSubmitterPair(submitter: DbSubmitterDto): Pair<Int, RestaurantApiType> {
        //TODO If null then bam
        return Pair(submitter.submitter_id, RestaurantApiType.getOrDefault(submitter.submitter_name))
    }
}