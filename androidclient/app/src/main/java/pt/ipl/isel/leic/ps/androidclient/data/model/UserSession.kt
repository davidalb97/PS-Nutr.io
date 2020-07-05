package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

class UserSession(
    val jwt: String,
    val submitterId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(jwt)
        parcel.writeInt(submitterId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserSession> {
        override fun createFromParcel(parcel: Parcel): UserSession {
            return UserSession(parcel)
        }

        override fun newArray(size: Int): Array<UserSession?> {
            return arrayOfNulls(size)
        }
    }

}