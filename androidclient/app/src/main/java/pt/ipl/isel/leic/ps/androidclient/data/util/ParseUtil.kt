package pt.ipl.isel.leic.ps.androidclient.data.util

import android.net.Uri
import android.os.Parcel
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import java.net.URI
import java.time.OffsetDateTime
import kotlin.reflect.KClass

fun <R: Any> readList(parcel: Parcel, kClass: KClass<R>): ArrayList<R> {
    return ArrayList<R>().also {
        parcel.readList(it as List<R>, kClass.java.classLoader)
    }
}

fun readBoolean(parcel: Parcel): Boolean {
    return parcel.readInt() != 0
}

fun writeBoolean(parcel: Parcel, boolean: Boolean) {
    parcel.writeInt(boolean.let { if (it) 1 else 0 })
}

fun readUri(parcel: Parcel): Uri? {
    //return parcel.readString()?.let { if(it.isNotBlank()) URI.create(it) else null }
    return parcel.readString()?.let { if(it.isNotBlank()) Uri.parse(it) else null }
}

fun writeUri(parcel: Parcel, uri: Uri?) {
    return parcel.writeString(uri?.toString() ?: "")
}

fun readTimestampWithTimeZone(parcel: Parcel): TimestampWithTimeZone? {
    return TimestampWithTimeZone.parse(parcel.readString())
}

fun writeTimestampWithTimeZone(parcel: Parcel, offsetDateTime: TimestampWithTimeZone?) {
    return parcel.writeString(offsetDateTime?.toString())
}
