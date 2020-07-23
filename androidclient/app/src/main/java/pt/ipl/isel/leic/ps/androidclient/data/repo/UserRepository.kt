package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.UserDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.InputUserLoginMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.InputUserRegisterMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class UserRepository(private val userDataSource: UserDataSource) {

    private val loginUserMapper = InputUserLoginMapper()
    private val registerUserMapper = InputUserRegisterMapper()

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
        userSessionConsumer: (UserSession) -> Unit,
        error: (VolleyError) -> Unit
    ) = userDataSource.login(
        username = userLogin.username,
        password = userLogin.password,
        error = error,
        consumerDto = { userSessionConsumer(loginUserMapper.mapToModel(it)) }
    )
}