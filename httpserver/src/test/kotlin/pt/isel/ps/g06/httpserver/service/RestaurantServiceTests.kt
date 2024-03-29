package pt.isel.ps.g06.httpserver.service

import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import pt.isel.ps.g06.httpserver.anyNonNull
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.here.HereRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.common.dto.RestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.RestaurantModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.FavoriteDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.ReportDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.DbRestaurantModelMapper
import pt.isel.ps.g06.httpserver.model.restaurant.Restaurant
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier
import pt.isel.ps.g06.httpserver.util.closableSequenceOf

class RestaurantServiceTests {
    private lateinit var restaurantRepository: RestaurantDbRepository
    private lateinit var restaurantModelMapper: RestaurantModelMapper
    private lateinit var apiSubmitterMapper: ApiSubmitterMapper
    private lateinit var favoriteRepository: FavoriteDbRepository
    private lateinit var reportRepository: ReportDbRepository
    private lateinit var dbRestaurantModelMapper: DbRestaurantModelMapper
    private lateinit var hereRestaurantApi: HereRestaurantApi

    private lateinit var service: RestaurantService

    @BeforeEach
    fun mockDependencies() {
        restaurantRepository = mock(RestaurantDbRepository::class.java)
        restaurantModelMapper = mock(RestaurantModelMapper::class.java)
        apiSubmitterMapper = mock(ApiSubmitterMapper::class.java)
        favoriteRepository = mock(FavoriteDbRepository::class.java)
        reportRepository = mock(ReportDbRepository::class.java)
        dbRestaurantModelMapper = mock(DbRestaurantModelMapper::class.java)
        hereRestaurantApi = mock(HereRestaurantApi::class.java)

        service = RestaurantService(
                dbRestaurantRepository = restaurantRepository,
                restaurantModelMapper = restaurantModelMapper,
                apiSubmitterMapper = apiSubmitterMapper,
                dbFavoriteDbRepository = favoriteRepository,
                dbReportDbRepository = reportRepository,
                dbRestaurantModelMapper = dbRestaurantModelMapper,
                hereRestaurantApi = hereRestaurantApi
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
        `when`(restaurantModelMapper.mapTo(firstApiDto)).thenReturn(firstApiRestaurant)
        `when`(restaurantModelMapper.mapTo(secondApiDto)).thenReturn(secondApiRestaurant)
        `when`(restaurantModelMapper.mapTo(firstDatabaseDto)).thenReturn(firstDatabaseRestaurant)

        //Setup search parameters
        val latitude = 10F
        val longitude = 10F
        val name = "Some name"
        val radius = 150
        val skip = 0
        val count = 10

        //Setup api search result
        `when`(hereRestaurantApi.searchNearbyRestaurants(
                latitude = anyNonNull(),
                longitude = anyNonNull(),
                radiusMeters = anyNonNull(),
                name = anyNonNull(),
                skip = anyNonNull(),
                count = anyNonNull()
        )).thenReturn(sequenceOf(firstApiDto, secondApiDto))

        //Setup database search result
        `when`(restaurantRepository.getAllByCoordinates(
                latitude = anyNonNull(),
                longitude = anyNonNull(),
                radius = anyNonNull(),
                skip = anyNonNull(),
                count = anyNonNull()
        )).thenReturn(closableSequenceOf(firstDatabaseDto))

        //Call to action
        val nearbyRestaurants = service.getNearbyRestaurants(
                latitude,
                longitude,
                name,
                radius,
                skip,
                count
        ).toList()

        //Assert
        Assert.assertEquals(2, nearbyRestaurants.size)
        Assert.assertNotNull(nearbyRestaurants.find { it.identifier.value.submissionId == databaseSubmissionId })
    }

    @Test
    fun `searching a restaurant with an outdated restaurant identifier should still return the database one`() {
        val submitter = 1
        val apiSubmissionIdentifier = "ABC"

        val restaurantDatabaseResult = mock(DbRestaurantDto::class.java)
        val expected = mock(Restaurant::class.java)

        `when`(apiSubmitterMapper.getApiType(submitter)).thenReturn(RestaurantApiType.Here)
        `when`(restaurantRepository.getApiRestaurant(submitter, apiSubmissionIdentifier)).thenReturn(restaurantDatabaseResult)
        `when`(restaurantModelMapper.mapTo(restaurantDatabaseResult)).thenReturn(expected)

        val result = service.getRestaurant(submitter, null, apiSubmissionIdentifier)

        verify(restaurantRepository, times(1)).getApiRestaurant(submitter, apiSubmissionIdentifier)
        //Restaurant should never be searched on API since a database restaurant was returned
        verifyNoInteractions(hereRestaurantApi)
        Assert.assertEquals(expected, result)
    }
}