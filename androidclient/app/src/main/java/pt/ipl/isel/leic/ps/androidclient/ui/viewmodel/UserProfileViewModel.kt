package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.userRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.User

class UserProfileViewModel() : ARecyclerViewModel<User>() {
    constructor(parcel: Parcel) : this() {
    }

    /*fun createUserProfile() {
        userRepository.createUser()
    }*/

    override fun update() {
        userRepository.getUserById(1)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        TODO()
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