package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

class UserRegister(
    val email: String,
    val username: String,
    val password: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(username)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserRegister> {
        override fun createFromParcel(parcel: Parcel) = UserRegister(parcel)

        override fun newArray(size: Int): Array<UserRegister?> {
            return arrayOfNulls(size)
        }
    }
}