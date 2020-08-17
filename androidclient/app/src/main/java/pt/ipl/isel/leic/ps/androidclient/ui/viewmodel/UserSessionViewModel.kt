package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.userRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class UserSessionViewModel : ViewModel() {

    fun register(
        userRegister: UserRegister,
        onSuccess: (UserSession) -> Unit,
        onError: (VolleyError) -> Unit
    ) = userRepository.registerUser(
        userReg = userRegister,
        userSessionConsumer = onSuccess,
        error = onError
    )

    fun login(
        userLogin: UserLogin,
        onSuccess: (UserSession) -> Unit,
        onError: (VolleyError) -> Unit
    ) = userRepository.loginUser(
        userLogin = userLogin,
        onSuccess = onSuccess,
        onError = onError
    )
}