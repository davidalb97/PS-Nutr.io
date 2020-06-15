package pt.ipl.isel.leic.ps.androidclient.data.util

import java.util.*

//This class is a work around OffsetDateTime.parse requires API level 26
class TimestampWithTimeZone(
    val year: Int,
    val month: Int,
    val day: Int,
    val hours: Int,
    val minutes: Int,
    val seconds: Int,
    val millis: Int,
    val timeZone: String
) {

    companion object {

        fun parse(formatted: String?): TimestampWithTimeZone? {
            if (formatted.isNullOrEmpty()) return null

            //TODO remove and fix this!
            //Hotfix for restaurant/meal info creation date problem
            if(formatted.toDoubleOrNull() != null) return null

            //TODO remove and fix this!
            //Hotfix for invalid conversion
            val formatterFix = formatted.replace("T", " ")

            var split = formatterFix.split(" ")
            val date = split[0].split("-")
            val year = date[0]
            val month = date[1]
            val day = date[2]
            split = formatterFix.split("+")
            val timeZone = split[1]
            split = split[0].split(".")
            val millis = split[1]
            split = split[0].split(" ")
            split = split[1].split(":")
            val hours = split[0]
            val minutes = split[1]
            val seconds = split[2]
            return TimestampWithTimeZone(
                year = year.toInt(),
                month = month.toInt(),
                day = day.toInt(),
                hours = hours.toInt(),
                minutes = minutes.toInt(),
                seconds = seconds.toInt(),
                millis = millis.toInt(),
                timeZone = timeZone
            )
        }

        fun now(): TimestampWithTimeZone {

            return TimestampWithTimeZone(
                year = Calendar.getInstance().get(Calendar.YEAR),
                month = Calendar.getInstance().get(Calendar.MONTH),
                day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                minutes = Calendar.getInstance().get(Calendar.MINUTE),
                seconds = Calendar.getInstance().get(Calendar.SECOND),
                millis = Calendar.getInstance().get(Calendar.MILLISECOND),
                //TODO Get user's time zone to TimestampWithTimeZone ctor
                timeZone = ""
            )
        }
    }

    override fun toString() = "$year-$month-$day $hours:$minutes:$seconds.$millis+$timeZone"
}