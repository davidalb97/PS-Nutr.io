package pt.isel.ps.g06.httpserver.dataAccess.db

import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.server.InvalidApplicationStartupException
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbApiDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ApiDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import java.util.stream.Collectors
import javax.annotation.PostConstruct

@Repository
data class ApiSubmitterMapper(
        private val submitterDbRepository: SubmitterDbRepository,
        private val apiDbRepository: ApiDbRepository,
        private val databaseContext: DatabaseContext
) {
    private lateinit var apiSubmitters: Map<Int, RestaurantApiType>

    @PostConstruct
    protected fun createMap() {
        val submitters: Map<Int, RestaurantApiType> = apiDbRepository
                .getApisByName(RestaurantApiType.values().map { it.toString() })
                .collect(Collectors.toMap({ it.submitter_id }, ::buildApiType))

        if (submitters.size != RestaurantApiType.values().size) {
            throw InvalidApplicationStartupException("Insufficient RestaurantApi submitters in Database! \n" +
                    "Required: ${RestaurantApiType.values().size} \n" +
                    "Found: ${submitters.size}"
            )
        }

        apiSubmitters = submitters

        //Force close because this method is never intercepted by Database context cleanup
        databaseContext.close()
    }


    fun getApiType(id: Int): RestaurantApiType? = apiSubmitters[id]

    fun getSubmitter(type: RestaurantApiType) = apiSubmitters.filterValues { it == type }.keys.firstOrNull()

    private fun buildApiType(apiDto: DbApiDto): RestaurantApiType {
        return RestaurantApiType
                .getOrNull(apiDto.api_name)
                ?: throw  InvalidApplicationStartupException("Failed to map string '${apiDto.api_name}' to a valid RestaurantApiType.")
    }
}