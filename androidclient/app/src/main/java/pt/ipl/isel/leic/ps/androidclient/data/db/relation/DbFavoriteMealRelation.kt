package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbApiMealJunction
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbFavoriteMealJunction

data class DbFavoriteMealRelation(
    @Embedded val entity: DbFavoriteMealEntity,
    @Relation(
        parentColumn = DbFavoriteMealEntity.primaryKeyName,
        entityColumn = DbIngredientEntity.mealKeyName/*,
        entity = DbIngredientEntity::class*/,
        associateBy = Junction(DbFavoriteMealJunction::class)
    )
    val ingredients: List<DbIngredientEntity>
)