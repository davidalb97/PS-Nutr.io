package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class UserLogin(
    val userName: String,
    val passWord: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userName)
        parcel.writeString(passWord)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserLogin> {
        override fun createFromParcel(parcel: Parcel): UserLogin {
            return UserLogin(parcel)
        }

        override fun newArray(size: Int): Array<UserLogin?> {
            return arrayOfNulls(size)
        }
    }
}