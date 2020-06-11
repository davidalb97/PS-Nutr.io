package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantInfoDto
import pt.isel.ps.g06.httpserver.model.Votes

class DbRestaurantInfoDto(
        val restaurantItem: DbRestaurantItemDto,
        val cuisines: Collection<String>,
        val restaurantMeals: Collection<DbRestaurantMealItemDto>
) : RestaurantInfoDto(
        id = "${restaurantItem.restaurant.submission_id}", //TODO Rest Identifier
        name = restaurantItem.restaurant.restaurant_name,
        latitude = restaurantItem.restaurant.latitude,
        longitude = restaurantItem.restaurant.longitude,
        votes = Votes(
                positive = restaurantItem.public.votes.positive_count,
                negative = restaurantItem.public.votes.negative_count
        ),
        userVote = restaurantItem.public.userVote,
        isFavorite = restaurantItem.isFavorite
)