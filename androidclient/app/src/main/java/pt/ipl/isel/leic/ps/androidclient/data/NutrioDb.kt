package pt.ipl.isel.leic.ps.androidclient.data

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.ipl.isel.leic.ps.androidclient.ROOM_DB_VERSION
import pt.ipl.isel.leic.ps.androidclient.data.dao.CuisineDao
import pt.ipl.isel.leic.ps.androidclient.data.dao.InsulinProfileDao
import pt.ipl.isel.leic.ps.androidclient.data.dao.MealDao
import pt.ipl.isel.leic.ps.androidclient.data.dao.RestaurantDao
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.source.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

@Database(
    entities =
    [
        //Restaurant::class,
        Meal::class,
        //Cuisine::class,
        InsulinProfile::class
    ],
    version = ROOM_DB_VERSION
)
abstract class NutrioDb : RoomDatabase() {

    //abstract fun restaurantDao(): RestaurantDao

    abstract fun mealDao(): MealDao

    //abstract fun cuisineDao(): CuisineDao

    abstract fun insulinProfileDao(): InsulinProfileDao
}