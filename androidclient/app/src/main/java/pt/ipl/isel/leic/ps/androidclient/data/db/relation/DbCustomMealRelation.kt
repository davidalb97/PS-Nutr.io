package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbCustomMealJunction
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbFavoriteMealJunction

data class DbCustomMealRelation(
    @Embedded val entity: DbCustomMealEntity,
    @Relation(
        parentColumn = DbCustomMealEntity.primaryKeyName,
        entityColumn = DbIngredientEntity.mealKeyName/*,
        entity = DbIngredientEntity::class*/,
        associateBy = Junction(DbCustomMealJunction::class)
    )
    val ingredients: List<DbIngredientEntity>
)