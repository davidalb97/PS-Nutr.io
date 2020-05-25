package pt.ipl.isel.leic.ps.androidclient.data

import androidx.room.TypeConverter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Ingredient
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.source.model.MealInfo

class DbConverters {

    @TypeConverter
    fun mealInfoToString(mealInfo: MealInfo): String =
        jacksonObjectMapper().writeValueAsString(mealInfo)

    @TypeConverter
    fun stringToMealInfo(mealInfoString: String): MealInfo =
        jacksonObjectMapper().readValue(
            mealInfoString,
            MealInfo::class.java
        )
}