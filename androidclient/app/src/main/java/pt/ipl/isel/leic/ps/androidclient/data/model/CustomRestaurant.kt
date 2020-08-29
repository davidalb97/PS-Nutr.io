package pt.ipl.isel.leic.ps.androidclient.data.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

class CustomRestaurant : RestaurantInfo, Parcelable {

    constructor(
        dbId: Long?,
        id: String?,
        name: String,
        latitude: Float,
        longitude: Float,
        imageUri: Uri?,
        cuisines: List<Cuisine>
    ) : super(
        dbId = dbId,
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        votes = null,
        isFavorite = false,
        isVotable = false,
        imageUri = imageUri,
        source = Source.CUSTOM,
        creationDate = null,
        cuisines = cuisines,
        meals = emptyList(),
        suggestedMeals = emptyList()
    )

    constructor(parcel: Parcel) : super(parcel)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomRestaurant> {
        override fun createFromParcel(parcel: Parcel): CustomRestaurant {
            return CustomRestaurant(parcel)
        }

        override fun newArray(size: Int): Array<CustomRestaurant?> {
            return arrayOfNulls(size)
        }
    }
}