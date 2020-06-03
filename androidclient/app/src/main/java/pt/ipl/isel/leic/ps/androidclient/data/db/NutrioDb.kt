package pt.ipl.isel.leic.ps.androidclient.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.ipl.isel.leic.ps.androidclient.ROOM_DB_VERSION
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.InsulinProfileDao
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.CustomMealDao
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.IngredientDao
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMeal
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredient

@Database(
    entities =
    [
        //Restaurant::class,
        DbCustomMeal::class,
        DbIngredient::class,
        //Cuisine::class,
        DbInsulinProfile::class
    ],
    version = ROOM_DB_VERSION
)
abstract class NutrioDb : RoomDatabase() {

    //abstract fun restaurantDao(): RestaurantDao

    abstract fun customMealDao(): CustomMealDao

    abstract fun ingredientDao(): IngredientDao

    //abstract fun cuisineDao(): CuisineDao

    abstract fun insulinProfileDao(): InsulinProfileDao
}