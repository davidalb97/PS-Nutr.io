package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.userRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.UserLogin
import pt.ipl.isel.leic.ps.androidclient.data.model.UserRegister
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class UserProfileViewModel() : ARecyclerViewModel<UserLogin>() {
    constructor(parcel: Parcel) : this()

    var userId: Int? = null

    fun register(
        userRegister: UserRegister,
        userSessionConsumer: (UserSession) -> Unit
    ) = userRepository.registerUser(
        userReg = userRegister,
        userSessionConsumer = userSessionConsumer,
        error = onError
    )

    fun login(
        userLogin: UserLogin,
        userSessionConsumer: (UserSession) -> Unit
    ) = userRepository.loginUser(
        userLogin = userLogin,
        userSessionConsumer = userSessionConsumer,
        error = onError
    )


    override fun update() {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        TODO("Save UserProfileViewModel to bundle")
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserProfileViewModel> {

        override fun createFromParcel(parcel: Parcel): UserProfileViewModel {
            return UserProfileViewModel(parcel)
        }

        override fun newArray(size: Int): Array<UserProfileViewModel?> {
            return arrayOfNulls(size)
        }

    }
}