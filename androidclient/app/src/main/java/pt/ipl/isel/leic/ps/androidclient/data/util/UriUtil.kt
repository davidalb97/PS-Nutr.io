package pt.ipl.isel.leic.ps.androidclient.data.util

import android.net.Uri

fun Uri.Builder.appendQueryParameter(key: String, value: Double): Uri.Builder {
    return this.appendQueryParameter(key, "$value")
}

fun Uri.Builder.appendQueryNotNullParameter(key: String, value: Int?): Uri.Builder {
    return value?.let { this.appendQueryParameter(key, "$value") } ?: this
}

fun Uri.Builder.appendQueryNotNullParameter(key: String, value: String?): Uri.Builder {
    return value?.let { this.appendQueryParameter(key, value) } ?: this
}

fun Uri.Builder.appendPath(path: Int): Uri.Builder {
    return this.appendPath("$path")
}

fun <T> Uri.Builder.appendQueryNotNullListParameter(
    key: String,
    list: Collection<T>?
): Uri.Builder {
    return if (list?.isNotEmpty() == true) this.appendQueryParameter(
        key,
        list.joinToString(separator = ",")
    )
    else this
}