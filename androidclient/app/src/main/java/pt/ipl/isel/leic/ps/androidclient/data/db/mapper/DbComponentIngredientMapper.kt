package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import android.net.Uri
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentIngredientEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Favorites
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

class DbComponentIngredientMapper {

    fun mapToModel(entity: DbComponentIngredientEntity) = MealIngredient(
        dbId = entity.primaryKey,
        dbMealId = entity.mealKey,
        submissionId = entity.submissionId,
        name = entity.name,
        imageUri = entity.imageUri?.let { Uri.parse(it) },
        carbs = entity.carbs,
        amount = entity.amount,
        favorites = Favorites(
            isFavorable = entity.isFavorable,
            isFavorite = entity.isFavorite,
        ),
        unit = WeightUnits.values()[entity.unit],
        isMeal = false,
        source = Source.values()[entity.sourceOrdinal]
    )

    fun mapToEntity(model: MealIngredient) = DbComponentIngredientEntity(
        submissionId = model.submissionId,
        name = model.name,
        carbs = model.carbs,
        amount = model.amount,
        unit = model.unit.ordinal,
        imageUri = model.imageUri?.toString(),
        sourceOrdinal = model.source.ordinal,
        isFavorable = model.favorites.isFavorable,
        isFavorite = model.favorites.isFavorite
    ).also { dto ->
        dto.primaryKey = model.dbId ?: DbComponentIngredientEntity.DEFAULT_DB_ID
        dto.mealKey = model.dbMealId ?: DbMealItemEntity.DEFAULT_DB_ID
    }

    fun mapToListModel(relations: List<DbComponentIngredientEntity>) =
        relations.map(this::mapToModel)

    fun mapToListEntity(relations: List<MealIngredient>) = relations.map(this::mapToEntity)
}