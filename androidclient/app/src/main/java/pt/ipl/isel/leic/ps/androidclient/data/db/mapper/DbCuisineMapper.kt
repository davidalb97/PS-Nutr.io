package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbCuisineEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine

class DbCuisineMapper {

    fun mapToModel(entity: DbCuisineEntity) = Cuisine(
        dbId = entity.primaryKey,
        dbMealId = entity.mealKey,
        dbRestaurantId = entity.restaurantKey,
        name = entity.name
    )

    fun mapToRestaurantEntity(model: Cuisine) = DbCuisineEntity(
        name = model.name
    ).also { dto ->
        dto.primaryKey = model.dbId
        dto.mealKey = model.dbMealId
        dto.restaurantKey = model.dbRestaurantId
    }

    fun mapToListModel(relations: List<DbCuisineEntity>) = relations.map(this::mapToModel)

    fun mapToListEntity(entities: List<Cuisine>) = entities.map(this::mapToRestaurantEntity)
}