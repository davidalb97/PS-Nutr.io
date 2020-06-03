package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.Ingredient

class IngredientMapper {

    fun mapToModel(dto: DbIngredient): Ingredient =
        Ingredient(
            dto.ingredientId,
            dto.name
        )

    fun mapToListModel(dtos: List<DbIngredient>): List<Ingredient> =
        dtos.map { mapToModel(it) }
}