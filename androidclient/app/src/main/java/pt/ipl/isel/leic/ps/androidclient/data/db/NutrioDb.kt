package pt.ipl.isel.leic.ps.androidclient.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.ipl.isel.leic.ps.androidclient.ROOM_DB_VERSION
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.*
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbApiMealJunction
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbCustomMealJunction
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbFavoriteMealJunction

@Database(
    entities =
    [
        //Restaurant::class,
        DbApiMealEntity::class,
        DbApiMealJunction::class,
        DbCustomMealEntity::class,
        DbCustomMealJunction::class,
        DbFavoriteMealEntity::class,
        DbFavoriteMealJunction::class,
        DbIngredientEntity::class,
        //Cuisine::class,
        InsulinProfileEntity::class
    ],
    version = ROOM_DB_VERSION
)
abstract class NutrioDb : RoomDatabase() {

    //abstract fun restaurantDao(): RestaurantDao

    abstract fun customMealDao(): CustomMealDao

    abstract fun favoriteMealDao(): FavoriteMealDao



    abstract fun ingredientDao(): IngredientDao

    //abstract fun cuisineDao(): CuisineDao

    abstract fun insulinProfileDao(): InsulinProfileDao
}