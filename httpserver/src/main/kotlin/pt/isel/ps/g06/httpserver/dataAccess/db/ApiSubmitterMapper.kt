package pt.isel.ps.g06.httpserver.dataAccess.db

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.server.InvalidApplicationStartupException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ApiDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import javax.annotation.PostConstruct

@Repository
data class ApiSubmitterMapper(
        private val submitterDbRepository: SubmitterDbRepository,
        private val apiDbRepository: ApiDbRepository
) {
    private lateinit var apiSubmitters: Map<Int, RestaurantApiType>

    @PostConstruct
    protected fun createMap() {
        val submitters = apiDbRepository
                .getApisByName(RestaurantApiType.values().map { it.toString() })
                .associate(this::buildSubmitterPair)

        if (submitters.size != RestaurantApiType.values().size) {
            throw InvalidApplicationStartupException("Insufficient RestaurantApi submitters in Database! \n" +
                    "Required: ${RestaurantApiType.values().size} \n" +
                    "Found: ${submitters.size}"
            )
        }

        apiSubmitters = submitters
    }


    fun getApiType(id: Int): RestaurantApiType? = apiSubmitters[id]

    fun getSubmitter(type: RestaurantApiType) = apiSubmitters.filterValues { it == type }.keys.firstOrNull()

    private fun buildSubmitterPair(apiDto: DbApiDto): Pair<Int, RestaurantApiType> {

        val type = RestaurantApiType
                .getOrNull(apiDto.api_name)
                ?: throw  InvalidApplicationStartupException("Failed to map string '${apiDto.api_name}' to a valid RestaurantApiType.")

        return Pair(apiDto.submitter_id, type)
    }
}