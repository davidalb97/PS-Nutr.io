package pt.isel.ps.g06.httpserver.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.util.Assert
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.ZomatoRestaurantApi
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.mapper.RestaurantApiMapper
import pt.isel.ps.g06.httpserver.dataAccess.api.restaurant.model.RestaurantApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.DbRestaurantRepository

class RestaurantServiceTests {
    @Test
    fun shouldReturnEmptySetWhenCoordinatesAreNull() {
        val reason = "Providing invalid coordinates to search nearby restaurants should return an empty set."

        val service = RestaurantService(
                mock(DbRestaurantRepository::class.java),
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
        val dbRepository = mock(DbRestaurantRepository::class.java)
        `when`(dbRepository.getRestaurantById(anyInt())).thenReturn(null)

        val api = mock(ZomatoRestaurantApi::class.java)
        `when`(api.getRestaurantInfo(anyInt())).thenReturn(null)

        val apiRepository = mock(RestaurantApiMapper::class.java)
        `when`(apiRepository.getRestaurantApi(RestaurantApiType.Zomato)).thenReturn(api)

        val result = RestaurantService(
                dbRepository,
                apiRepository
        ).getRestaurant(1, null)

        Assertions.assertNull(result)
    }
}