package pt.ipl.isel.leic.ps.androidclient.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.ipl.isel.leic.ps.androidclient.ROOM_DB_VERSION
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.*

@Database(
    entities =
    [
        DbApiMealEntity::class,
        DbCustomMealEntity::class,
        DbFavoriteMealEntity::class,
        DbIngredientEntity::class,
        DbInsulinProfileEntity::class
    ],
    version = ROOM_DB_VERSION
)
abstract class NutrioDb : RoomDatabase() {

    //abstract fun restaurantDao(): RestaurantDao

    abstract fun apiMealDao(): ApiMealDao

    abstract fun customMealDao(): CustomMealDao

    abstract fun favoriteMealDao(): FavoriteMealDao

    abstract fun ingredientDao(): IngredientDao

    //abstract fun cuisineDao(): CuisineDao

    abstract fun insulinProfileDao(): InsulinProfileDao
}