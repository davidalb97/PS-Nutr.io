package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class Meal(
    val submissionId: Int,
    val name: String,
    val votes: Votes,
    val isFavorite: Boolean?,
    val imageUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        votes = parcel.readParcelable<Votes>(Votes::class.java.classLoader)!!,
        isFavorite = parcel.readBoolean(),
        imageUrl = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        //Does not need to close resources, using flag 0
        parcel.writeParcelable(votes, 0)
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