package pt.ipl.isel.leic.ps.androidclient.util

import android.net.Uri
import android.os.Parcel
import kotlin.reflect.KClass

fun <R : Any> Parcel.readListCompat(kClass: KClass<R>): ArrayList<R> {
    return ArrayList<R>().also {
        this.readList(it as List<R>, kClass.java.classLoader)
    }
}

fun Parcel.readBooleanCompat(): Boolean {
    return this.readInt() != 0
}

fun Parcel.writeBooleanCompat(boolean: Boolean) {
    this.writeInt(boolean.let { if (it) 1 else 0 })
}

fun Parcel.readUri(): Uri? {
    //return this.readString()?.let { if(it.isNotBlank()) URI.create(it) else null }
    return this.readString()?.let { if (it.isNotBlank()) Uri.parse(it) else null }
}

fun Parcel.writeUri(uri: Uri?) {
    this.writeString(uri?.toString() ?: "")
}

fun Parcel.readTimestampWithTimeZone(): TimestampWithTimeZone? {
    return this.readString()?.let {
        TimestampWithTimeZone.parse(
            it
        )
    }
}

fun Parcel.writeTimestampWithTimeZone(offsetDateTime: TimestampWithTimeZone?) {
    this.writeString(offsetDateTime?.toString())
}
