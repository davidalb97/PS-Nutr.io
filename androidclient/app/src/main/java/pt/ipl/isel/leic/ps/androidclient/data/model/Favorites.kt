package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.readBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.writeBooleanCompat

class Favorites(
    var isFavorable: Boolean,
    var isFavorite: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        isFavorable = parcel.readBooleanCompat(),
        isFavorite = parcel.readBooleanCompat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeBooleanCompat(isFavorable)
        parcel.writeBooleanCompat(isFavorite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Favorites> {
        override fun createFromParcel(parcel: Parcel) = Favorites(parcel)

        override fun newArray(size: Int): Array<Favorites?> {
            return arrayOfNulls(size)
        }
    }
}