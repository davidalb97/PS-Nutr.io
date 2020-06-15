package pt.ipl.isel.leic.ps.androidclient.data.db.mapper

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbUserEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.User

class DbUserMapper {

    fun mapToModel(dto: DbUserEntity) =
        User(
            userId = dto.userId,
            name = dto.name
        )

    fun mapToEntity(model: User) =
        DbUserEntity(
            userId = model.userId,
            name = model.name
        )
}