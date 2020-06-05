package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

data class FavoriteMeal(
    val dbId: Long = 0,
    val submissionId: Int,
    val name: String,
    val carbs: Int,
    val amount: Int,
    val unit: String,
    val votes: Votes,
    val imageUrl: String?,
    val ingredients: List<Ingredient>
): Parcelable {
    constructor(parcel: Parcel) : this(
        dbId = parcel.readLong(),
        submissionId = parcel.readInt(),
        name = parcel.readString()!!,
        carbs = parcel.readInt(),
        amount = parcel.readInt(),
        unit = parcel.readString()!!,
        votes = parcel.readParcelable<Votes>(Votes::class.java.classLoader)!!,
        imageUrl = parcel.readString(),
        ingredients = ArrayList<Ingredient>().also {
            parcel.readList(it as List<Ingredient>, Ingredient::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(dbId)
        parcel.writeInt(submissionId)
        parcel.writeString(name)
        parcel.writeInt(carbs)
        parcel.writeInt(amount)
        //Does not need to close resources, using flag 0
        parcel.writeParcelable(votes, 0)
        parcel.writeString(imageUrl)
        parcel.writeList(ingredients)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FavoriteMeal> {
        override fun createFromParcel(parcel: Parcel): FavoriteMeal {
            return FavoriteMeal(parcel)
        }

        override fun newArray(size: Int): Array<FavoriteMeal?> {
            return arrayOfNulls(size)
        }
    }
}