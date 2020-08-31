package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.restaurant

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.cuisine.CuisinesInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.favorite.FavoritesInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.meal.MealItemInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.SimplifiedUserInput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.vote.VotesInput

class RestaurantInfoInput(
    identifier: String,
    name: String,
    image: Uri?,
    favorites: FavoritesInput,
    latitude: Float,
    longitude: Float,
    votes: VotesInput,
    isReportable: Boolean,
    val cuisines: CuisinesInput,
    val creationDate: String?,
    val meals: Collection<MealItemInput>,
    val suggestedMeals: Collection<MealItemInput>,
    val createdBy: SimplifiedUserInput
) : RestaurantItemInput(
    identifier = identifier,
    name = name,
    image = image,
    favorites = favorites,
    latitude = latitude,
    longitude = longitude,
    votes = votes,
    isReportable = isReportable
)