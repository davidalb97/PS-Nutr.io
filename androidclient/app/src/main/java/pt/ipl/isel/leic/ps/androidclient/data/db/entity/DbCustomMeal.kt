package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Relation

class DbCustomMeal(
    @Embedded val meal: DbMeal,
    @Relation(
        parentColumn = "mealName",
        entityColumn = "ingredientMealName",
        entity = DbIngredient::class
    )
    val ingredients: List<DbIngredient>
)