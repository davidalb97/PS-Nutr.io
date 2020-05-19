package pt.isel.ps.g06.httpserver.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.HereRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.ZomatoRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.Location
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.dto.ZomatoRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.RestaurantResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.RestaurantDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantDto
import pt.isel.ps.g06.httpserver.db.Constants
import pt.isel.ps.g06.httpserver.db.RepoAsserts
import pt.isel.ps.g06.httpserver.model.Meal
import pt.isel.ps.g06.httpserver.model.Restaurant
import java.net.http.HttpClient
import java.net.http.HttpResponse
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

    private fun mockClient(value: String): HttpClient {
        val mockHttpClient = Mockito.mock(HttpClient::class.java)
        val mockHttpResponse = Mockito.mock(HttpResponse::class.java) as HttpResponse<String>
        `when`(mockHttpResponse.body()).thenReturn(value)
        `when`(mockHttpClient.sendAsync(any(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(CompletableFuture.completedFuture(mockHttpResponse))
        `when`(mockHttpClient.send(any(), HttpResponse.BodyHandlers.ofString()))
                .thenReturn(mockHttpResponse)
        return mockHttpClient
    }

    @Test
    fun `Should search restaurants from Zomato and db`() {
//        val expectedHereApiRspStr = ""
//        val hereApi = HereRestaurantApi(
//                mockClient(expectedHereApiRspStr),
//                HereUriBuilder(),
//                objMapper
//        )
//        val expectedZomatoApiRspStr = ""
//        val zomatoApi = ZomatoRestaurantApi(
//                mockClient(expectedZomatoApiRspStr),
//                ZomatoUriBuilder(),
//                objMapper
//        )

        //Mock api returning countApiDtoCount
        val countApiDtoCount = 3
        val zomatoApi = mock(ZomatoRestaurantApi::class.java)
        `when`(zomatoApi.searchNearbyRestaurants(anyFloat(), anyFloat(), anyInt(), anyString()))
                .thenReturn(CompletableFuture.completedFuture((1..countApiDtoCount).map {
                    //mock(ZomatoRestaurantDto::class.java)
                    ZomatoRestaurantDto("$it", "$it", "cuisine1", Location(
                            "adress$it",
                            "locality$it",
                            "city$it",
                            it,
                            0.0F + it,
                            0.0F + it
                    ))
                }))
        val restaurantApiRepo = RestaurantApiMapper(zomatoApi, mock(HereRestaurantApi::class.java))

        //Mock db returning countDbDtoCount
        val db = mock(RestaurantDbRepository::class.java)
        val countDbDtoCount = 3
        `when`(db.getAllByCoordinates(anyFloat(), anyFloat(), anyInt()))
                .thenReturn((1..countDbDtoCount).map {
                    DbRestaurantDto(it, "$it", 0.0F + it, 0.0F + it)
                    //mock(DbRestaurantDto::class.java)
                })

        //Mock restaurant mapper that maps each dto type
        val restaurantMapper = mock(RestaurantResponseMapper::class.java)
        //Api dto mapper
        val expectedApiRestaurants = (1..countApiDtoCount).map {
            Restaurant("$it", "$it", 0.0F + it, 0.0F + it, lazy { emptyList<String>() }, lazy { emptyList<Meal>() })
        }
        `when`(restaurantMapper.mapTo(isA(ZomatoRestaurantDto::class.java)))
                //.thenReturn(expectedApiRestaurants.first(), *(expectedApiRestaurants.drop(1)).toTypedArray())
                .thenReturn(expectedApiRestaurants[0], expectedApiRestaurants[1], expectedApiRestaurants[2])
        //Db dto mapper
        val expectedDbRestaurants = (1..countDbDtoCount).map {
            Restaurant("$it", "$it", 0.0F + it, 0.0F + it, lazy { emptyList<String>() }, lazy { emptyList<Meal>() })
        }
        `when`(restaurantMapper.mapTo(isA(DbRestaurantDto::class.java)))
                //.thenReturn(expectedDbRestaurants.first(), *(expectedDbRestaurants.drop(1)).toTypedArray())
                .thenReturn(expectedDbRestaurants[0], expectedDbRestaurants[1], expectedDbRestaurants[2])

        val test1 = zomatoApi.searchNearbyRestaurants(0F, 0F, 1, "")
                .get()
        val test2 = test1.map(restaurantMapper::mapTo)
        val test3 = db.getAllByCoordinates(0F, 0F, 1)
        val test4 = test3.map(restaurantMapper::mapTo)

        val restaurants = RestaurantService(db, restaurantApiRepo, restaurantMapper).getNearbyRestaurants(
                anyFloat(),
                anyFloat(),
                anyString(),
                anyInt(),
                RestaurantApiType.Zomato.toString()
        )
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