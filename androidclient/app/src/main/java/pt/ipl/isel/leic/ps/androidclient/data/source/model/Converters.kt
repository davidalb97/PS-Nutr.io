package pt.ipl.isel.leic.ps.androidclient.data.source.model

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}