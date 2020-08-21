package pt.isel.ps.g06.httpserver.security.converter

import pt.isel.ps.g06.httpserver.springConfig.InsulinProfilesSecret

class StringColumnCryptoConverter(
        private val insulinProfilesSecret: InsulinProfilesSecret
): AColumnCryptoConverter<String>(insulinProfilesSecret) {
    override fun stringToType(str: String): String {
        TODO("Not yet implemented")
    }

    override fun typeToString(type: String): String {
        TODO("Not yet implemented")
    }
}