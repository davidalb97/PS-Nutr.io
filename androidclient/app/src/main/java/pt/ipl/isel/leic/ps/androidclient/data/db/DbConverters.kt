package pt.ipl.isel.leic.ps.androidclient.data.db

import androidx.room.TypeConverter
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo

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