package pt.isel.ps.g06.httpserver.model

class RestaurantInfo(
        identifier: RestaurantIdentifier,
        name: String,
        latitude: Float,
        longitude: Float,
        image: String?,
        val cuisines: Lazy<Collection<String>>,
        val meals: Lazy<Collection<MealItem>>,
        votes: Votes,
        userVote: Boolean?,
        isFavorite: Boolean
): RestaurantItem(
        identifier = identifier,
        name = name,
        latitude = latitude,
        longitude = longitude,
        image = image,
        votes = votes,
        userVote = userVote,
        isFavorite = isFavorite
)