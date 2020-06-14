package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.Embedded
import androidx.room.Relation
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.*

data class DbRestaurantInfoRelation(
    @Embedded val entity: DbRestaurantInfoEntity,
    @Relation(
        parentColumn = DbRestaurantInfoEntity.primaryKeyName,
        entityColumn = DbMealItemEntity.restaurantKeyName
    )
    val meals: List<DbMealItemEntity>,
    @Relation(
        parentColumn = DbRestaurantInfoEntity.primaryKeyName,
        entityColumn = DbCuisineEntity.restaurantKeyName
    )
    val cuisines: List<DbCuisineEntity>
)