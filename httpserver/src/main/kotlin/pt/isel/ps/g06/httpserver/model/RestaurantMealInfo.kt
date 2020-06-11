package pt.isel.ps.g06.httpserver.model

class RestaurantMealInfo(
        restaurantMealId: Int,
        mealId: Int,
        name: String,
        isFavorite: Boolean,
        image: String?,
        userVote: Boolean?,
        votes: Votes,
        val carbs: Int,
        val amount: Int,
        val unit: String,
        val ingredients: Collection<MealIngredient>,
        val cuisines: Collection<String>,
        val portions: Collection<Portion>
): RestaurantMealItem(
        restaurantMealId = restaurantMealId,
        mealId = mealId,
        name = name,
        isFavorite = isFavorite,
        image = image,
        userVote = userVote,
        votes = votes
)