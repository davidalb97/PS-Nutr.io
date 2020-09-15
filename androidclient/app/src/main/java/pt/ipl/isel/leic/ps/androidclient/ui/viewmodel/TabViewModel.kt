package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.util.readListCompat

class TabViewModel : ViewModel, Parcelable {

    var tags: MutableList<String?>

    constructor(): super() {
        tags = mutableListOf()
    }

    constructor(parcel: Parcel) : super() {
        tags = parcel.readListCompat(String::class).toMutableList()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(tags.toList())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TabViewModel> {
        override fun createFromParcel(parcel: Parcel) = TabViewModel(parcel)

        override fun newArray(size: Int): Array<TabViewModel?> {
            return arrayOfNulls(size)
        }
    }
}