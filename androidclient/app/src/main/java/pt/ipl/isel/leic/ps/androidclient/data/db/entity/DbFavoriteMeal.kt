package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class DbFavoriteMeal(
    val mealId: String,
    @Embedded val meal: DbMeal,
    @Relation(
        parentColumn = "mealName",
        entityColumn = "ingredientMealName"
    )
    val ingredients: List<DbIngredient>
)