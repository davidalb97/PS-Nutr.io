package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbApiMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbApiMealJunction

data class DbApiMealRelation(
    @Embedded val entity: DbApiMealEntity,
    @Relation(
//        associateBy = DbApiMealJunction::class,
        parentColumn = DbApiMealEntity.primaryKeyName,
        entityColumn = DbIngredientEntity.mealKeyName/*,
        entity = DbIngredientEntity::class*/,
        associateBy = Junction(DbApiMealJunction::class)
    )
    val ingredients: List<DbIngredientEntity>
)