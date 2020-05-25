package pt.ipl.isel.leic.ps.androidclient.data.source.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.stream.Stream

@Entity(tableName = "Meal")
data class Meal(
    @PrimaryKey val identifier: String,
    val name: String,
    val image_url: String,
    val mealInfo: MealInfo
)