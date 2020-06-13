package pt.isel.ps.g06.httpserver.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import pt.isel.ps.g06.httpserver.anyNonNull
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.HereRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.ZomatoRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.here.HereResultItem
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.RestaurantResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantMealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.Cuisine
import pt.isel.ps.g06.httpserver.db.Constants
import pt.isel.ps.g06.httpserver.db.RepoAsserts
import pt.isel.ps.g06.httpserver.model.*
import java.time.OffsetDateTime
import java.util.concurrent.CompletableFuture

class RestaurantServiceTests {

    @Autowired
    lateinit var jdbi: Jdbi

    @Autowired
    lateinit var restaurantDbRepo: RestaurantDbRepository

    @Autowired
    lateinit var asserts: RepoAsserts

    @Autowired
    lateinit var const: Constants

    @Autowired
    lateinit var objMapper: ObjectMapper

    /**
     * Mocks a RestaurantService with supplied mapped results
     */
    private fun mockRestaurantSearchService(
            expectedDbRestaurants: List<Restaurant> = emptyList(),
            expectedZomatoApiRestaurants: List<Restaurant> = emptyList(),
            expectedHereApiRestaurants: List<Restaurant> = emptyList(),
            callCount: Int
    ): RestaurantService {
        val callList = (1..callCount)
        val totalExpectedDbRestaurants = callList.flatMap { expectedDbRestaurants }
        val totalExpectedZomatoApiRestaurants = callList.flatMap { expectedZomatoApiRestaurants }
        val totalExpectedHereApiRestaurants = callList.flatMap { expectedHereApiRestaurants }

        val restaurantDbRepository = mock(RestaurantDbRepository::class.java)
        `when`(restaurantDbRepository.getAllByCoordinates(anyFloat(), anyFloat(), anyInt()))
                .thenReturn((1..expectedDbRestaurants.size).map { mock(DbRestaurantDto::class.java) }.asSequence())

        val zomatoApi = mock(ZomatoRestaurantApi::class.java)
        `when`(zomatoApi.searchNearbyRestaurants(anyFloat(), anyFloat(), anyInt(), anyString()))
                .thenReturn(CompletableFuture.completedFuture((1..expectedZomatoApiRestaurants.size).map {
                    mock(ZomatoRestaurantDto::class.java)
                }))

        val hereApi = mock(HereRestaurantApi::class.java)
        `when`(hereApi.searchNearbyRestaurants(anyFloat(), anyFloat(), anyInt(), anyString()))
                .thenReturn(CompletableFuture.completedFuture((1..expectedHereApiRestaurants.size).map {
                    mock(HereResultItem::class.java)
                }))

        //Mock restaurant item mapper that maps each dto type
        val restaurantItemMapper = mock(RestaurantResponseMapper::class.java)
        //Db dto mapper
        `when`(restaurantItemMapper.mapTo(anyNonNull<DbRestaurantDto>()))
                .thenReturn(totalExpectedDbRestaurants[0], *totalExpectedDbRestaurants.drop(1).toTypedArray())
        //Api Zomato dto mapper
        `when`(restaurantItemMapper.mapTo(anyNonNull<ZomatoRestaurantDto>()))
                .thenReturn(totalExpectedZomatoApiRestaurants[0], *totalExpectedZomatoApiRestaurants.drop(1).toTypedArray())
        //Api Here dto mapper
        `when`(restaurantItemMapper.mapTo(anyNonNull<HereResultItem>()))
                .thenReturn(totalExpectedHereApiRestaurants[0], *totalExpectedHereApiRestaurants.drop(1).toTypedArray())

        //Mock api repo
        val restaurantApiRepo = RestaurantApiMapper(zomatoApi, hereApi)

        return RestaurantService(
                restaurantDbRepository,
                mock(RestaurantMealDbRepository::class.java),
                restaurantApiRepo,
                restaurantItemMapper,
                mock(ApiSubmitterMapper::class.java)
        )
    }

    /**
     * Returns a list of mapped Restaurants to be used on a service
     */
    private fun mockMappedRestaurant(from: Int, to: Int, namePrefix: String): List<Restaurant> {
        return (from..to).map { idx ->
            Restaurant(
                    identifier = lazy {
                        RestaurantIdentifier(
                                submitterId = idx,
                                submissionId = idx,
                                apiId = "Test-$idx"
                        )
                    },
                    name = "$namePrefix-Test-$idx",
                    latitude = 0.0F + idx,
                    longitude = 0.0F + idx,
                    votes = Votes(idx, idx),
                    image = null,
                    userVote = { if (idx % 2 == 0) VoteState.POSITIVE else VoteState.NEGATIVE },
                    isFavorite = { idx % 2 == 0 },
                    cuisines = emptySequence<Cuisine>(),
                    meals = emptySequence<RestaurantMeal>(),
                    suggestedMeals = emptySequence<Meal>(),
                    creationDate = lazy { OffsetDateTime.now() },
                    creatorInfo = lazy {
                        Creator(
                                name = "Test-$idx",
                                creationDate = OffsetDateTime.now(),
                                image = null
                        )
                    }
            )
        }
    }

    @Test
    fun `Should return existing db restaurants ignoring api restaurants`() {
        val expectedDbRestaurants = mockMappedRestaurant(1, 3, "Db")
        val expectedZomatoApiRestaurants = mockMappedRestaurant(1, 3, RestaurantApiType.Zomato.toString())
        val expectedHereApiRestaurants = mockMappedRestaurant(1, 3, RestaurantApiType.Here.toString())
        val service = mockRestaurantSearchService(
                expectedDbRestaurants,
                expectedZomatoApiRestaurants,
                expectedHereApiRestaurants,
                2
        )
        //Db / Zomato test
        var restaurants = service.getNearbyRestaurants(
                anyFloat(),
                anyFloat(),
                anyString(),
                anyInt(),
                RestaurantApiType.Zomato.toString(),
                anyInt()
        )
        //Should only contain db restaurants
        Assertions.assertTrue(restaurants.toList().containsAll(expectedDbRestaurants))
        Assertions.assertTrue(restaurants.none { expectedZomatoApiRestaurants.contains(it) })

        restaurants = service.getNearbyRestaurants(
                anyFloat(),
                anyFloat(),
                anyString(),
                anyInt(),
                RestaurantApiType.Here.toString(),
                anyInt()
        )
        //Should only contain db restaurants
        Assertions.assertTrue(restaurants.toList().containsAll(expectedDbRestaurants))
        Assertions.assertTrue(restaurants.none { expectedHereApiRestaurants.contains(it) })
    }

    @Test
    fun `Should return existing db restaurants and new api restaurants`() {
        val expectedDbRestaurants = mockMappedRestaurant(1, 3, "Db")
        val expectedZomatoApiRestaurants = mockMappedRestaurant(4, 6, RestaurantApiType.Zomato.toString())
        val expectedHereApiRestaurants = mockMappedRestaurant(4, 6, RestaurantApiType.Here.toString())
        val service = mockRestaurantSearchService(
                expectedDbRestaurants,
                expectedZomatoApiRestaurants,
                expectedHereApiRestaurants,
                2
        )
        //Db / Zomato test
        var restaurants = service.getNearbyRestaurants(
                anyFloat(),
                anyFloat(),
                anyString(),
                anyInt(),
                RestaurantApiType.Zomato.toString(),
                anyInt()
        )
        //Should only contain db restaurants
        Assertions.assertTrue(restaurants.toList().containsAll(expectedDbRestaurants))
        Assertions.assertTrue(restaurants.toList().containsAll(expectedZomatoApiRestaurants))

        restaurants = service.getNearbyRestaurants(
                anyFloat(),
                anyFloat(),
                anyString(),
                anyInt(),
                RestaurantApiType.Here.toString(),
                anyInt()
        )
        //Should only contain db restaurants
        Assertions.assertTrue(restaurants.toList().containsAll(expectedDbRestaurants))
        Assertions.assertTrue(restaurants.toList().containsAll(expectedHereApiRestaurants))
    }

    @Test
    fun shouldReturnEmptySetWhenCoordinatesAreNull() {
        /*
        val reason = "Providing invalid coordinates to search nearby restaurants should return an empty set."
        val service = RestaurantService(
                mock(RestaurantDbRepository::class.java),
                mock(RestaurantApiMapper::class.java)
        )

        val nullLatitude = service.getNearbyRestaurants(
                null,
                -10F,
                100,
                RestaurantApiType.Zomato.name
        )

        val nullLongitude = service.getNearbyRestaurants(
                -10F,
                null,
                100,
                RestaurantApiType.Zomato.name
        )

        Assert.isTrue(nullLatitude.isEmpty(), reason)
        Assert.isTrue(nullLongitude.isEmpty(), reason)
         */

        TODO("Commented compilation errors")
    }

//    @Test
//    fun shouldFilterApiValuesWhenTheyAlreadyExistInDatabase() {
//        //Mock database and api values
//        val dbName = "That sushi place"
//        val dbValues = listOf(DbRestaurantDto(1, dbName, 10F, 10F))
//        val apiValues = listOf(ApiRestaurantDto(
//                1,
//                "That GOOD sushi place",
//                "",
//                "Japanese",
//                mock(Location::class.java)
//        ))
//
//        //Mock restaurant database repository
//        val dbRepository = mock(DbRestaurantRepository::class.java)
//        `when`(dbRepository.getRestaurantsByCoordinates(
//                anyFloat(),
//                anyFloat(),
//                anyInt())
//        ).thenReturn(dbValues)
//
//        //Mock api repository + restaurant api
//        val apiRepository = mock(RestaurantApiRepository::class.java)
//        val zomatoApi = mock(ZomatoRestaurantApi::class.java)
//        `when`(zomatoApi.searchRestaurants(anyFloat(), anyFloat(), anyInt())).thenReturn(apiValues)
//        `when`(apiRepository.getRestaurantApi(anyString())).thenReturn(zomatoApi)
//
//        val result = RestaurantService(dbRepository, apiRepository).getNearbyRestaurants(
//                10F,
//                10F,
//                100,
//                null
//        )
//
//        assertNotEquals(result.size, apiValues.size + dbValues.size)
//        assertEquals(result.first().name, dbName)
//    }

    @Test
    fun searchRestaurantByIdShouldReturnNullWhenNoneIsFound() {
        /*
        val dbRepository = mock(RestaurantDbRepository::class.java)
        `when`(dbRepository.getById(anyInt())).thenReturn(null)

        val api = mock(ZomatoRestaurantApi::class.java)
        `when`(api.getRestaurantInfo(anyInt())).thenReturn(null)

        val apiRepository = mock(RestaurantApiMapper::class.java)
        `when`(apiRepository.getRestaurantApi(RestaurantApiType.Zomato)).thenReturn(api)

        val result = RestaurantService(
                dbRepository,
                apiRepository
        ).getRestaurant(1, null)

        Assertions.assertNull(result)
         */
        TODO("Commented compilation errors")
    }
}