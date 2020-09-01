package pt.ipl.isel.leic.ps.androidclient.data.model

import android.os.Parcel
import android.os.Parcelable
import pt.ipl.isel.leic.ps.androidclient.util.readBooleanCompat
import pt.ipl.isel.leic.ps.androidclient.util.writeBooleanCompat

data class Votes(
    val isVotable: Boolean,
    var userHasVoted: VoteState,
    val positive: Int,
    val negative: Int
) : Parcelable {

    constructor(parcel: Parcel) : this(
        isVotable = parcel.readBooleanCompat(),
        userHasVoted = VoteState.values()[parcel.readInt()],
        positive = parcel.readInt(),
        negative = parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeBooleanCompat(isVotable)
        parcel.writeInt(userHasVoted.ordinal)
        parcel.writeInt(positive)
        parcel.writeInt(negative)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Votes> {
        fun defaultVotes() = Votes(
            isVotable = false,
            userHasVoted = VoteState.NOT_VOTED,
            positive = 0,
            negative = 0
        )

        override fun createFromParcel(parcel: Parcel): Votes {
            return Votes(parcel)
        }

        override fun newArray(size: Int): Array<Votes?> {
            return arrayOfNulls(size)
        }
    }
}