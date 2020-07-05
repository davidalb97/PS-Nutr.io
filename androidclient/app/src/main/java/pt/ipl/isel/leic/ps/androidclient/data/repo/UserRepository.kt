package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.UserDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.UserLoginMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.UserRegisterMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

const val DEFAULT_DB_USER = 3

class UserRepository(private val userDataSource: UserDataSource) {

    private val loginUserMapper = UserLoginMapper()
    private val registerUserMapper = UserRegisterMapper()

    //TODO - onErrors
    fun registerUser(userReg: UserRegister) =
        userDataSource.register(userReg.email, userReg.username, userReg.password, {})

    fun loginUser(userLogin: UserLogin, userSession: (UserSession) -> Unit) =
        userDataSource.login(
            userLogin.username,
            userLogin.password,
            {},
            { userSession(loginUserMapper.mapToModel(it)) }
        )
}