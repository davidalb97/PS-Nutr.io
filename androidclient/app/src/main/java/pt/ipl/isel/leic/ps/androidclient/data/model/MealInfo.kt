package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import java.net.URI

class MealInfo(
    val dbId: Long,
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val votes: Votes,
    val imageUrl: URI?,
    val ingredients: List<MealInfo>
): Parcelable {

    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        carbs = parcel.readInt(),
        amount = parcel.readInt(),
        unit = parcel.readString()!!,
        votes = parcel.readParcelable(Votes::class.java.classLoader)!!,
        imageUrl = parcel.readString()?.let { if(it.isNotEmpty()) URI.create(it) else null },
        ingredients = ArrayList<MealInfo>().also {
            parcel.readList(it as List<MealInfo>, MealInfo::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        parcel.writeString(unit)
        parcel.writeParcelable(votes, 0)
        parcel.writeString(imageUrl?.toString() ?: "")
        parcel.writeList(ingredients)
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