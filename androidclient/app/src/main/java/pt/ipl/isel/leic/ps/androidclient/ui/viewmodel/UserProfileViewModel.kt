package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.userRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.User

class UserProfileViewModel() : ARecyclerViewModel<User>() {
    constructor(parcel: Parcel) : this() {
    }



    var userId: Int? = null

    fun createUserProfile(user: User) = userRepository.createUser(user)

    fun getUserProfile(id: Int) = userRepository.getUserById(id)

    fun getAllProfiles() = userRepository.getAllUsers()

    override fun update() {
        liveDataHandler.set(userRepository.getAllUsers()) {
            userRepository.userMapper.mapToModel(it)
        }


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