package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.stream.Stream

@Entity(tableName = "Meal")
data class Meal(
    @PrimaryKey val identifier: String,
    val name: String,
    val image_url: String,
    val mealInfo: MealInfo
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        TODO("mealInfo")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(identifier)
        parcel.writeString(name)
        parcel.writeString(image_url)
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