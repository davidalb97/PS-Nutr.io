package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbPortionEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Portions

class DbPortionMapper {

    fun mapToModel(
        dbPortionEntity: DbPortionEntity,
        dbMealInfoMapper: DbMealInfoEntity,
        portions: List<Float>
    ) = Portions(
        dbId = dbPortionEntity.primaryKey,
        dbMealId = dbPortionEntity.mealKey,
        userPortion = dbMealInfoMapper.userPortion,
        allPortions = portions
    )

    fun mapToListEntity(model: Portions): List<DbPortionEntity> =
        model.allPortions.map(::DbPortionEntity)
}