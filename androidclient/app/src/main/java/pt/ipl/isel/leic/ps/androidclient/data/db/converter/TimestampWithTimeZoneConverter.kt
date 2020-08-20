package pt.ipl.isel.leic.ps.androidclient.data.db.converter

import androidx.room.TypeConverter
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

//This class is a work around OffsetDateTime.parse requires API level 26
class TimestampWithTimeZoneConverter {

    @TypeConverter
    fun toDate(dateString: String?): TimestampWithTimeZone? {
        return dateString?.let { TimestampWithTimeZone.parse(it) }
    }

    @TypeConverter
    fun toDateString(date: TimestampWithTimeZone?): String? {
        return date?.toString()
    }
}