package pt.isel.ps.g06.httpserver.security.converter

import pt.isel.ps.g06.httpserver.springConfig.InsulinProfilesSecret

class IntColumnCryptoConverter(
        private val insulinProfilesSecret: InsulinProfilesSecret
): AColumnCryptoConverter<Int>(insulinProfilesSecret) {
    override fun stringToType(str: String): Int = str.toInt()

    override fun typeToString(type: Int): String = type.toString()
}