package pt.ipl.isel.leic.ps.androidclient.data.util

//This class is a work around OffsetDateTime.parse requires API level 26
class TimestampWithTimeZone(
    val year: String,
    val month: String,
    val day: String,
    val hours: String,
    val minutes: String,
    val seconds: String,
    val millis: String,
    val timeZone: String
) {

    companion object {

        fun parse(formatted: String?): TimestampWithTimeZone? {
            if(formatted == null) return null
            var split = formatted.split(" ")
            val date = split[0].split("-")
            val year = date[0]
            val month = date[1]
            val day = date[2]
            split = formatted.split("+")
            val timeZone = split[1]
            split = split[0].split(".")
            val millis = split[1]
            split = split[0].split(":")
            val hours = split[0]
            val minutes = split[1]
            val seconds = split[2]
            return TimestampWithTimeZone(
                year = year,
                month = month,
                day = day,
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                millis = millis,
                timeZone = timeZone
            )
        }
    }

    override fun toString() = "$year-$month-$day $hours:$minutes$seconds.$millis+$timeZone"
}