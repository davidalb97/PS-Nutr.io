package pt.ipl.isel.leic.ps.androidclient.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pt.ipl.isel.leic.ps.androidclient.ROOM_DB_VERSION
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.IngredientDao
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.InsulinProfileDao
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.MealDao
import pt.ipl.isel.leic.ps.androidclient.data.model.Ingredient
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal

@Database(
    entities =
    [
        //Restaurant::class,
        Meal::class,
        Ingredient::class,
        //Cuisine::class,
        InsulinProfileDto::class
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