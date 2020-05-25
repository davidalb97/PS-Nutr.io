package pt.ipl.isel.leic.ps.androidclient.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pt.ipl.isel.leic.ps.androidclient.ROOM_DB_VERSION
import pt.ipl.isel.leic.ps.androidclient.data.dao.*
import pt.ipl.isel.leic.ps.androidclient.data.source.model.*

@Database(
    entities =
    [
        //Restaurant::class,
        Meal::class,
        Ingredient::class,
        //Cuisine::class,
        InsulinProfile::class
    ],
    version = ROOM_DB_VERSION
)
@TypeConverters(DbConverters::class)
abstract class NutrioDb : RoomDatabase() {

    //abstract fun restaurantDao(): RestaurantDao

    abstract fun mealDao(): MealDao

    abstract fun ingredientDao(): IngredientDao

    //abstract fun cuisineDao(): CuisineDao

    abstract fun insulinProfileDao(): InsulinProfileDao
}