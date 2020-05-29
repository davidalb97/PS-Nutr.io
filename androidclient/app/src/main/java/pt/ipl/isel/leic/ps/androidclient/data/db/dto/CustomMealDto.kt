package pt.ipl.isel.leic.ps.androidclient.data.db.dto

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CustomMeal")
data class CustomMealDto(
    @PrimaryKey val name: String,
    val mealQuantity: Int,
    val glucoseAmount: Int,
    val carboAmount: Int
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(mealQuantity)
        parcel.writeInt(glucoseAmount)
        parcel.writeInt(carboAmount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomMealDto> {
        override fun createFromParcel(parcel: Parcel): CustomMealDto {
            return CustomMealDto(parcel)
        }

        override fun newArray(size: Int): Array<CustomMealDto?> {
            return arrayOfNulls(size)
        }
    }
}