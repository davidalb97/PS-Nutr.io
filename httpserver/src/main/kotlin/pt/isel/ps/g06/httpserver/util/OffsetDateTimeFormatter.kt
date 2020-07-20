package pt.isel.ps.g06.httpserver.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

class OffsetDateTimeSupport {

    companion object {
        /**
         * Parses a PostgreSQL date time with time zone formatted string into an OffsetDateTime
         * @param dateTimeWithTimeZone A PostgreSQL date time with time zone formatted string
         */
        fun parseFromDateTimeWithWithTimeZone(dateTimeWithTimeZone: String): OffsetDateTime {
            val formatter = DateTimeFormatterBuilder()
                    .append(DateTimeFormatter.ISO_DATE)
                    .appendLiteral(' ')
                    .appendPattern("HH:mm:ss")
                    .appendPattern("[.SSSSSSSSS][.SSSSSSS][.SSSSSSS][.SSSSSS][.SSSSS][.SSSS][.SSS][.SS][.S]")
                    .appendOffset("+HH", "Z")
                    .toFormatter()
            return OffsetDateTime.parse(dateTimeWithTimeZone, formatter)
        }
    }
}