package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCustomMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbFavoriteMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbApiMealJunction
import pt.ipl.isel.leic.ps.androidclient.data.db.junction.DbFavoriteMealJunction

data class DbFavoriteMealRelation(
    @Embedded val entity: DbFavoriteMealEntity,
    @Relation(
        parentColumn = DbFavoriteMealEntity.primaryKeyName,
        entityColumn = DbIngredientEntity.mealKeyName,
        associateBy = Junction(DbFavoriteMealJunction::class)
    )
    val ingredients: List<DbIngredientEntity>
)