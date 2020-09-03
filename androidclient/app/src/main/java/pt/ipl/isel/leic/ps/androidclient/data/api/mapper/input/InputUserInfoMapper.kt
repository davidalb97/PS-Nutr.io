package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.user.UserInfoInput
import pt.ipl.isel.leic.ps.androidclient.data.model.UserInfo

class InputUserInfoMapper {

    fun mapToModel(dto: UserInfoInput): UserInfo =
        UserInfo(
            email = dto.email,
            username = dto.username,
            image = dto.userImage
        )
}