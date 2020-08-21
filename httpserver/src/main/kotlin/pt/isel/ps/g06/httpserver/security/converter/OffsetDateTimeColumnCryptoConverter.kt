package pt.isel.ps.g06.httpserver.security.converter

import pt.isel.ps.g06.httpserver.springConfig.InsulinProfilesSecret
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter.ISO_DATE


class OffsetDateTimeColumnCryptoConverter(
        private val insulinProfilesSecret: InsulinProfilesSecret
) : AColumnCryptoConverter<OffsetDateTime>(insulinProfilesSecret) {

    override fun stringToType(str: String): OffsetDateTime = OffsetDateTime.parse(str, ISO_DATE)

    override fun typeToString(type: OffsetDateTime): String = type.toString()
}