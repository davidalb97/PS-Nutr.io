package pt.ipl.isel.leic.ps.androidclient.data.db.dto

import androidx.room.Entity
import androidx.room.PrimaryKey
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo

@Entity(tableName = "FavoriteMeal")
data class DbFavoriteMealDto(
    @PrimaryKey val identifier: String,
    val name: String,
    val image_url: String
)