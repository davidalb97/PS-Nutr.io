package pt.ipl.isel.leic.ps.androidclient.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.ipl.isel.leic.ps.androidclient.ROOM_DB_VERSION
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.InsulinProfileDao
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.CustomMealDao
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.FavoriteMealDao
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.IngredientDao
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.*

@Database(
    entities =
    [
        //Restaurant::class,
        DbCustomMeal::class,
        DbFavoriteMeal::class,
        DbIngredient::class,
        //Cuisine::class,
        DbInsulinProfile::class
    ],
    version = ROOM_DB_VERSION
)
abstract class NutrioDb : RoomDatabase() {

    //abstract fun restaurantDao(): RestaurantDao

    abstract fun CustomMealDao(): CustomMealDao

    abstract fun FavoriteMealDao(): FavoriteMealDao

    abstract fun ingredientDao(): IngredientDao

    //abstract fun cuisineDao(): CuisineDao

    abstract fun insulinProfileDao(): InsulinProfileDao
}