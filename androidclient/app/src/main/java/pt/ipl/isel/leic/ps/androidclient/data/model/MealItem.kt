package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import java.net.URI

data class MealItem(
    val dbId: Long,
    val submissionId: Int,
    val name: String,
    val imageUrl: URI?,
    val votes: Votes,
    val isFavorite: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        imageUrl = parcel.readString()?.let { URI.create(it) },
        votes = parcel.readParcelable(Votes::class.java.classLoader)!!,
        isFavorite = parcel.readInt() != 0
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        parcel.writeString(imageUrl?.toString() ?: "")
        parcel.writeParcelable(votes, 0)
        parcel.writeInt(isFavorite.let { if (it) 1 else 0 })
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MealItem> {
        override fun createFromParcel(parcel: Parcel): MealItem {
            return MealItem(parcel)
        }

        override fun newArray(size: Int): Array<MealItem?> {
            return arrayOfNulls(size)
        }
    }
}