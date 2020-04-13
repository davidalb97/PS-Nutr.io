package pt.isel.ps.g06.httpserver.dataAccess.api

abstract class AUriBuilder {

    internal fun <T> param(name: String, t: T?): String =
            (t?.let { "&$name=$t" } ?: "")

    internal fun param(name: String, tArray: Array<String>?): String =
            (tArray?.let { "&$name=${it.joinToString(",")}" } ?: "")
}