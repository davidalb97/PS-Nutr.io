package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import java.net.URI

class UserInfoInput(
    val email: String,
    val username: String,
    val userImage: URI?
)