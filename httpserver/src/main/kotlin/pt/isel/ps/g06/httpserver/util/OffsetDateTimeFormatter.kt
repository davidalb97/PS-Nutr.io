package pt.isel.ps.g06.httpserver.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

fun parsePostgresql(dateTimeWithTimeZone: String): OffsetDateTime {
    val formatter = DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_DATE)
            .appendLiteral(' ')
            .appendPattern("HH:mm:ss")
            .appendPattern("[.SSSSSSSSS][.SSSSSSS][.SSSSSSS][.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]")
            .appendOffset("+HH", "Z")
            .toFormatter()
    return OffsetDateTime.parse(dateTimeWithTimeZone, formatter)
}