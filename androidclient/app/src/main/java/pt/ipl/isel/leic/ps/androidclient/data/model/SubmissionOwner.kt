package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable

class SubmissionOwner(
    val id: Int
): Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubmissionOwner> {
        override fun createFromParcel(parcel: Parcel): SubmissionOwner {
            return SubmissionOwner(parcel)
        }

        override fun newArray(size: Int): Array<SubmissionOwner?> {
            return arrayOfNulls(size)
        }
    }
}