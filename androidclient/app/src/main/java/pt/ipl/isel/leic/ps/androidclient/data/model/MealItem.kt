package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.util.readBoolean
import pt.ipl.isel.leic.ps.androidclient.data.util.readUri
import pt.ipl.isel.leic.ps.androidclient.data.util.writeBoolean
import pt.ipl.isel.leic.ps.androidclient.data.util.writeUri

open class MealItem(
    var dbId: Long,
    var dbRestaurantId: Long,
    val submissionId: Int,
    val name: String,
    open val carbs: Int?,
    open val amount: Int?,
    open val unit: String?,
    val imageUri: Uri?,
    val votes: Votes?,
    val isFavorite: Boolean,
    val isSuggested: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        dbRestaurantId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        carbs = parcel.readSerializable() as Int?,
        amount = parcel.readSerializable() as Int?,
        unit = parcel.readString(),
        imageUri = readUri(parcel),
        votes = parcel.readParcelable(Votes::class.java.classLoader),
        isFavorite = readBoolean(parcel),
        isSuggested = readBoolean(parcel)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeLong(dbRestaurantId)
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        parcel.writeSerializable(carbs)
        parcel.writeSerializable(amount)
        parcel.writeString(unit)
        writeUri(parcel, imageUri)
        parcel.writeParcelable(votes, flags)
        writeBoolean(parcel, isFavorite)
        writeBoolean(parcel, isSuggested)
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