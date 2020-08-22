package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.readBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.readUri
import pt.ipl.isel.leic.ps.androidclient.util.writeBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.writeUri

open class MealItem(
    var dbId: Long?,
    var dbRestaurantId: Long?,
    val submissionId: Int?,
    val restaurantSubmissionId: String?,
    val name: String,
    open val carbs: Int?,
    open val amount: Int?,
    open val unit: String?,
    val imageUri: Uri?,
    val votes: Votes?,
    var isFavorite: Boolean,
    var isVotable: Boolean,
    val isSuggested: Boolean,
    var source: Source
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readSerializable() as Long?,
        dbRestaurantId = parcel.readSerializable() as Long?,
        submissionId = parcel.readSerializable() as Int?,
        restaurantSubmissionId = parcel.readString(),
        name = parcel.readString()!!,
        carbs = parcel.readSerializable() as Int?,
        amount = parcel.readSerializable() as Int?,
        unit = parcel.readString(),
        imageUri = parcel.readUri(),
        votes = parcel.readParcelable(Votes::class.java.classLoader),
        isFavorite = parcel.readBooleanCompat(),
        isVotable = parcel.readBooleanCompat(),
        isSuggested = parcel.readBooleanCompat(),
        source = Source.values()[parcel.readInt()]
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(dbId)
        parcel.writeSerializable(dbRestaurantId)
        parcel.writeSerializable(submissionId)
        parcel.writeString(restaurantSubmissionId)
        parcel.writeString(name)
        parcel.writeSerializable(carbs)
        parcel.writeSerializable(amount)
        parcel.writeString(unit)
        parcel.writeUri(imageUri)
        parcel.writeParcelable(votes, flags)
        parcel.writeBooleanCompat(isFavorite)
        parcel.writeBooleanCompat(isVotable)
        parcel.writeBooleanCompat(isSuggested)
        parcel.writeInt(source.ordinal)
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