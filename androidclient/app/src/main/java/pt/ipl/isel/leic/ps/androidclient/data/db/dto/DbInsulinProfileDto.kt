package pt.ipl.isel.leic.ps.androidclient.data.db.dto

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "InsulinProfile")
data class DbInsulinProfileDto(
    @PrimaryKey val profile_name: String,
    val start_time: String,
    val end_time: String,
    val glucose_objective: Int,
    val glucoseAmountPerInsulin: Int,
    val carbsAmountPerInsulin: Int
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
        parcel.writeInt(glucoseAmountPerInsulin)
        parcel.writeInt(carbsAmountPerInsulin)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DbInsulinProfileDto> {
        override fun createFromParcel(parcel: Parcel): DbInsulinProfileDto {
            return DbInsulinProfileDto(parcel)
        }

        override fun newArray(size: Int): Array<DbInsulinProfileDto?> {
            return arrayOfNulls(size)
        }
    }
}