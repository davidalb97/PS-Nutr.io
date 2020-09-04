package pt.ipl.isel.leic.ps.androidclient.data.repo

import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.UserDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputUserInfoMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputUserLoginMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputUserRegisterMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output.OutputLoginMapper
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output.OutputRegisterMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.UserInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class UserRepository(private val userDataSource: UserDataSource) {

    private val inputUserLoginMapper = InputUserLoginMapper()
    private val inputUserRegisterMapper = InputUserRegisterMapper()
    private val inputUserInfoMapper = InputUserInfoMapper()
    private val outputRegisterMapper = OutputRegisterMapper()
    private val outputLoginMapper = OutputLoginMapper()

    fun registerUser(
        userReg: UserRegister,
        userSessionConsumer: (UserSession) -> Unit,
        error: (VolleyError) -> Unit
    ) = userDataSource.postRegister(
        registerOutput = outputRegisterMapper.mapToOutputModel(model = userReg),
        error = error,
        consumerDto = { userSessionConsumer(inputUserRegisterMapper.mapToModel(dto = it)) }
    )

    fun loginUser(
        userLogin: UserLogin,
        onSuccess: (UserSession) -> Unit,
        onError: (VolleyError) -> Unit
    ) = userDataSource.postLogin(
        loginOutput = outputLoginMapper.mapToOutputModel(model = userLogin),
        error = onError,
        consumerDto = { onSuccess(inputUserLoginMapper.mapToModel(it)) }
    )

    fun deleteAccount(
        userSession: UserSession,
        onSuccess: () -> Unit,
        onError: (VolleyError) -> Unit
    ) = userDataSource.deleteAccount(
        jwt = userSession.jwt,
        onSuccess = onSuccess,
        onError = onError,
    )

    fun requestUserInfo(
        userSession: UserSession,
        onSuccess: (UserInfo) -> Unit,
        onError: (VolleyError) -> Unit
    ) = userDataSource.getUserInfo(
        userSession.jwt,
        error = onError,
        consumerDto = { onSuccess(inputUserInfoMapper.mapToModel(it)) }
    )
}