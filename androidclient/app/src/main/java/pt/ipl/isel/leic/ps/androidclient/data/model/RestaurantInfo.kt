package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat
import pt.ipl.isel.leic.ps.androidclient.util.readTimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.util.writeTimestampWithTimeZone

open class RestaurantInfo : RestaurantItem, Parcelable {

    val creationDate: TimestampWithTimeZone?
    val cuisines: List<Cuisine>
    val meals: List<MealItem>
    val suggestedMeals: List<MealItem>
    var ownerId: Int? = 0

    constructor(
        dbId: Long?,
        id: String?,
        name: String,
        latitude: Float,
        longitude: Float,
        votes: Votes,
        favorites: Favorites,
        isReportable: Boolean,
        image: Uri?,
        source: Source,
        creationDate: TimestampWithTimeZone?,
        cuisines: List<Cuisine>,
        meals: List<MealItem>,
        suggestedMeals: List<MealItem>,
        ownerId: Int?
    ) : super(
        dbId = dbId,
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        votes = votes,
        favorites = favorites,
        isReportable = isReportable,
        image = image,
        source = source
    ) {
        this.creationDate = creationDate
        this.cuisines = cuisines
        this.meals = meals
        this.suggestedMeals = suggestedMeals
        this.ownerId = ownerId
    }

    constructor(parcel: Parcel) : super(parcel) {
        creationDate = parcel.readTimestampWithTimeZone()
        cuisines = parcel.readListCompat(Cuisine::class)
        meals = parcel.readListCompat(MealItem::class)
        suggestedMeals = parcel.readListCompat(MealItem::class)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeTimestampWithTimeZone(creationDate)
        parcel.writeList(cuisines)
        parcel.writeList(meals)
        parcel.writeList(suggestedMeals)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantInfo> {
        override fun createFromParcel(parcel: Parcel): RestaurantInfo {
            return RestaurantInfo(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantInfo?> {
            return arrayOfNulls(size)
        }
    }
}