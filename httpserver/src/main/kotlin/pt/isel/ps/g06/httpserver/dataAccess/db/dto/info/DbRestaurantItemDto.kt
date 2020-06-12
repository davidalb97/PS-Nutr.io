package pt.isel.ps.g06.httpserver.dataAccess.db.dto.info

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantDto
import pt.isel.ps.g06.httpserver.dataAccess.model.RestaurantItemDto
import pt.isel.ps.g06.httpserver.model.Votes

open class DbRestaurantItemDto(
        val restaurant: DbRestaurantDto,
        val submitterId: Int,
        val apiId: String?,
        val image: String?,
        isFavorite: Boolean?,
        val public: DbPublicDto
): RestaurantItemDto(
        id = "${restaurant.submission_id}",
        name = restaurant.restaurant_name,
        latitude = restaurant.latitude,
        longitude = restaurant.longitude,
        votes = Votes(
                positive = public.votes.positive_count,
                negative = public.votes.negative_count
        ),
        userVote = public.userVote,
        isFavorite = isFavorite
)