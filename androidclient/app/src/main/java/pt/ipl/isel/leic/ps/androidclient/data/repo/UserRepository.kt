package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.UserDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputUserInfoMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputUserLoginMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputUserRegisterMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.UserInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class UserRepository(private val userDataSource: UserDataSource) {

    private val loginUserMapper =
        InputUserLoginMapper()
    private val registerUserMapper =
        InputUserRegisterMapper()
    private val infoUserMapper =
        InputUserInfoMapper()

    fun registerUser(
        userReg: UserRegister,
        userSessionConsumer: (UserSession) -> Unit,
        error: (VolleyError) -> Unit
    ) = userDataSource.register(
        email = userReg.email,
        username = userReg.username,
        password = userReg.password,
        error = error,
        consumerDto = { userSessionConsumer(registerUserMapper.mapToModel(it)) }
    )

    fun loginUser(
        userLogin: UserLogin,
        onSuccess: (UserSession) -> Unit,
        onError: (VolleyError) -> Unit
    ) = userDataSource.login(
        username = userLogin.email,
        password = userLogin.password,
        error = onError,
        consumerDto = { onSuccess(loginUserMapper.mapToModel(it)) }
    )

    fun requestUserInfo(
        userSession: UserSession,
        onSuccess: (UserInfo) -> Unit,
        onError: (VolleyError) -> Unit
    ) = userDataSource.requestUserInfo(
        userSession.jwt,
        error = onError,
        consumerDto = { onSuccess(infoUserMapper.mapToModel(it)) }
    )
}