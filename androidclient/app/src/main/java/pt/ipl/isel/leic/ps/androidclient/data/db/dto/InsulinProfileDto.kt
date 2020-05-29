package pt.ipl.isel.leic.ps.androidclient.data.db.dto

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InsulinProfile")
data class InsulinProfileDto(
    @PrimaryKey val profile_name: String,
    val start_time: String,
    val end_time: String,
    val glucose_objective: Int,
    val glucose_amount: Int,
    val carbohydrate_amount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(profile_name)
        parcel.writeString(start_time)
        parcel.writeString(end_time)
        parcel.writeInt(glucose_objective)
        parcel.writeInt(glucose_amount)
        parcel.writeInt(carbohydrate_amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InsulinProfileDto> {
        override fun createFromParcel(parcel: Parcel): InsulinProfileDto {
            return InsulinProfileDto(parcel)
        }

        override fun newArray(size: Int): Array<InsulinProfileDto?> {
            return arrayOfNulls(size)
        }
    }
}