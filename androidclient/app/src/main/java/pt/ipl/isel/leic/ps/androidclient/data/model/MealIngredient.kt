package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealItemEntity
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.util.readBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.readUri
import pt.ipl.isel.leic.ps.androidclient.util.writeBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.writeUri

class MealIngredient : MealItem, Parcelable {

    var dbMealId: Long?
    var isMeal: Boolean

    constructor(
        dbMealId: Long?,
        isMeal: Boolean,
        dbId: Long?,
        submissionId: Int?,
        name: String,
        carbs: Int,
        amount: Float,
        favorites: Favorites,
        unit: WeightUnits,
        imageUri: Uri?,
        source: Source
    ) : super(
        dbId = dbId,
        dbRestaurantId = null,
        submissionId = submissionId,
        restaurantSubmissionId = null,
        name = name,
        carbs = carbs,
        amount = amount,
        unit = unit,
        votes = null,
        favorites = favorites,
        imageUri = imageUri,
        isVerified = false,
        isSuggested = false,
        isReportable = false,
        source = source
    ) {
        this.dbMealId = dbMealId
        this.isMeal = isMeal
    }

    constructor(parcel: Parcel) : super(parcel) {
        dbMealId = parcel.readSerializable() as Long?
        isMeal = parcel.readBooleanCompat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeSerializable(dbMealId)
        parcel.writeBooleanCompat(isMeal)
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