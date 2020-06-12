package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Meal(
    val dbId: Long,
    val submissionId: Int,
    val name: String,
    val isFavorite: Byte?,
    val imageUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        isFavorite = parcel.readByte(),
        imageUrl = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        //Does not need to close resources, using flag 0
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Meal> {
        override fun createFromParcel(parcel: Parcel): Meal {
            return Meal(parcel)
        }

        override fun newArray(size: Int): Array<Meal?> {
            return arrayOfNulls(size)
        }
    }
}