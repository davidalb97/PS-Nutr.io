package pt.isel.ps.g06.httpserver.service

import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import pt.isel.ps.g06.httpserver.anyNonNull
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.RestaurantResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier
import java.util.concurrent.CompletableFuture
import java.util.stream.Stream
import kotlin.streams.toList

@RunWith(SpringJUnit4ClassRunner::class)
class RestaurantServiceTests {
    lateinit var dbRestaurantRepository: RestaurantDbRepository
    lateinit var restaurantApiMapper: RestaurantApiMapper
    lateinit var restaurantResponseMapper: RestaurantResponseMapper
    lateinit var apiSubmitterMapper: ApiSubmitterMapper
    lateinit var dbFavoriteDbRepository: FavoriteDbRepository
    lateinit var dbReportDbRepository: ReportDbRepository

    lateinit var restaurantService: RestaurantService

    @BeforeEach
    fun mockDependencies() {
        dbRestaurantRepository = mock(RestaurantDbRepository::class.java)
        restaurantApiMapper = mock(RestaurantApiMapper::class.java)
        restaurantResponseMapper = mock(RestaurantResponseMapper::class.java)
        apiSubmitterMapper = mock(ApiSubmitterMapper::class.java)
        dbFavoriteDbRepository = mock(FavoriteDbRepository::class.java)
        dbReportDbRepository = mock(ReportDbRepository::class.java)

        restaurantService = RestaurantService(
                dbRestaurantRepository = dbRestaurantRepository,
                restaurantApiMapper = restaurantApiMapper,
                restaurantResponseMapper = restaurantResponseMapper,
                apiSubmitterMapper = apiSubmitterMapper,
                dbFavoriteDbRepository = dbFavoriteDbRepository,
                dbReportDbRepository = dbReportDbRepository
        )
    }

    @Test
    fun `nearby restaurants search should prioritize database results and filter duplicate API results`() {
        val databaseSubmissionId = 100

        //Setup restaurant dto
        val firstApiDto = mock(RestaurantDto::class.java)
        val secondApiDto = mock(RestaurantDto::class.java)
        val firstDatabaseDto = mock(DbRestaurantDto::class.java)

        //Setup restaurant models and their ids
        val firstApiRestaurant = mock(Restaurant::class.java)
        val secondApiRestaurant = mock(Restaurant::class.java)
        val firstDatabaseRestaurant = mock(Restaurant::class.java)

        `when`(firstApiRestaurant.identifier).thenReturn(lazy { RestaurantIdentifier(1, null, "HERE:1") })
        `when`(secondApiRestaurant.identifier).thenReturn(lazy { RestaurantIdentifier(1, null, "HERE:2") })
        `when`(firstDatabaseRestaurant.identifier).thenReturn(lazy { RestaurantIdentifier(1, databaseSubmissionId, "HERE:1") })

        //Setup response mapper result
        `when`(restaurantResponseMapper.mapTo(firstApiDto)).thenReturn(firstApiRestaurant)
        `when`(restaurantResponseMapper.mapTo(secondApiDto)).thenReturn(secondApiRestaurant)
        `when`(restaurantResponseMapper.mapTo(firstDatabaseDto)).thenReturn(firstDatabaseRestaurant)

        //Setup search parameters
        val latitude = 10F
        val longitude = 10F
        val name = "Some name"
        val radius = 150
        val apiType = "here"
        val skip = 0
        val count = 10

        //Setup api search result
        val restaurantApi = mock(RestaurantApi::class.java)
        `when`(restaurantApi.searchNearbyRestaurants(
                latitude,
                longitude,
                radius,
                name,
                skip,
                count
        )).thenReturn(CompletableFuture.completedFuture(listOf(firstApiDto, secondApiDto)))

        `when`(restaurantApiMapper.getRestaurantApi(anyNonNull())).thenReturn(restaurantApi)

        //Setup database search result
        `when`(dbRestaurantRepository.getAllByCoordinates(latitude, longitude, radius)).thenReturn(Stream.of(firstDatabaseDto))

        //Call to action
        val nearbyRestaurants = restaurantService.getNearbyRestaurants(
                latitude,
                longitude,
                name,
                radius,
                apiType,
                skip,
                count
        ).toList()

        //Assert
        Assert.assertEquals(2, nearbyRestaurants.size)
        Assert.assertNotNull(nearbyRestaurants.find { it.identifier.value.submissionId == databaseSubmissionId })
    }
}