package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.CustomRestaurantOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomRestaurant


class OutputCustomRestaurantMapper(private val cuisineMapper: OutputCuisineMapper) {

    fun mapToOutputModel(restaurant: CustomRestaurant) = CustomRestaurantOutput(
        name = restaurant.name,
        latitude = restaurant.latitude,
        longitude = restaurant.longitude,
        cuisines = cuisineMapper.mapToOutputModelCollection(restaurant.cuisines)
    )
}