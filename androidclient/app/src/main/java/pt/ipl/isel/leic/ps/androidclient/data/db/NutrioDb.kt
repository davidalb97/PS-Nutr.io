package pt.ipl.isel.leic.ps.androidclient.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pt.ipl.isel.leic.ps.androidclient.ROOM_DB_VERSION
import pt.ipl.isel.leic.ps.androidclient.data.db.dao.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.*

@Database(
    entities =
    [
        DbComponentIngredientEntity::class,
        DbComponentMealEntity::class,
        DbCuisineEntity::class,
        DbMealInfoEntity::class,
        DbMealItemEntity::class,
        DbPortionEntity::class,
        DbRestaurantInfoEntity::class,
        DbRestaurantItemEntity::class,
        InsulinProfileEntity::class
    ],
    version = ROOM_DB_VERSION
)
abstract class NutrioDb : RoomDatabase() {

    abstract fun restaurantInfoDao(): RestaurantInfoDao

    abstract fun mealInfoDao(): MealInfoDao

    abstract fun insulinProfileDao(): InsulinProfileDao
}