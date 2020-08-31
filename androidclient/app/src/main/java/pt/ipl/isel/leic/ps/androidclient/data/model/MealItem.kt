package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.util.*

//TODO replace var to val when fields are not changed
open class MealItem(
    var dbId: Long?,
    var dbRestaurantId: Long?,
    var submissionId: Int?,
    var restaurantSubmissionId: String?,
    var name: String,
    var carbs: Int,
    var amount: Float,
    var unit: WeightUnits,
    var imageUri: Uri?,
    var votes: Votes?,
    var isFavorite: Boolean,
    var isVotable: Boolean,
    var isSuggested: Boolean,
    var source: Source
) : Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readSerializable() as Long?,
        dbRestaurantId = parcel.readSerializable() as Long?,
        submissionId = parcel.readSerializable() as Int?,
        restaurantSubmissionId = parcel.readString(),
        name = parcel.readString()!!,
        carbs = parcel.readInt(),
        amount = parcel.readFloat(),
        unit = parcel.readWeightUnit(),
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
        parcel.writeInt(carbs)
        parcel.writeFloat(amount)
        parcel.writeWeightUnit(unit)
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