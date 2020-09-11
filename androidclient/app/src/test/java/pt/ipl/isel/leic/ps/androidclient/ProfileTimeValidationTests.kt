package pt.ipl.isel.leic.ps.androidclient

import org.junit.Assert
import org.junit.Test
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone
import java.text.SimpleDateFormat
import java.util.*


class ProfileTimeValidationTests {

    private fun mockProfile(start: String, end: String): InsulinProfile {
        return InsulinProfile(
            profileName = "",
            startTime = start,
            endTime = end,
            glucoseObjective = 0.0F,
            glucoseAmountPerInsulin = 0.0F,
            carbsAmountPerInsulin = 0.0F,
            modificationDate = TimestampWithTimeZone.now()
        )
    }

    private val EXISTING_PROFILE_05_TO_10 = mockProfile("05:00", "10:00")

    @Test
    fun `Should not insert profile when times overlap`() {
        Assert.assertTrue(EXISTING_PROFILE_05_TO_10.overlaps(mockProfile("00:01", "16:00")))
        Assert.assertTrue(EXISTING_PROFILE_05_TO_10.overlaps(mockProfile("01:00", "06:00")))
        Assert.assertTrue(EXISTING_PROFILE_05_TO_10.overlaps(mockProfile("06:00", "09:00")))
        Assert.assertTrue(EXISTING_PROFILE_05_TO_10.overlaps(mockProfile("06:00", "11:00")))
        Assert.assertTrue(EXISTING_PROFILE_05_TO_10.overlaps(mockProfile("03:00", "12:00")))
    }

    @Test
    fun `Should insert profile when times do not overlap`() {
        Assert.assertFalse(EXISTING_PROFILE_05_TO_10.overlaps(mockProfile("01:00", "04:00")))
        Assert.assertFalse(EXISTING_PROFILE_05_TO_10.overlaps(mockProfile("11:00", "14:00")))
    }

    private val timeFormat = SimpleDateFormat("HH:MM")

    fun <T> validateTimeComparator(supplier: (String) -> T) where T: Comparable<T> {
        var before = supplier("15:01")
        var after = supplier("15:2")

        Assert.assertTrue(before < after)
        Assert.assertTrue(after > before)
        before = supplier("15:1")
        after = supplier("15:02")
        Assert.assertTrue(before < after)
        Assert.assertTrue(after > before)

        before = supplier("19:02")
        after = supplier("19:36")

        Assert.assertTrue(after > before)
        Assert.assertTrue(after >= before)
        before = supplier("19:36")
        after = supplier("20:02")
        Assert.assertTrue(before < after)
        Assert.assertTrue(before <= after)

        before = supplier("19:02")
        val between = supplier("19:36")
        after = supplier("20:02")
        Assert.assertTrue(before.compareTo(between) <= 0 && after.compareTo(between) >= 0)
        Assert.assertTrue(between >= before && between <= after)
        Assert.assertTrue(between in before..after)
    }

    @Test
    fun `java's Date compareTo() should work`() {
        validateTimeComparator(timeFormat::parse)
    }

    @Test
    fun `format to int compareTo() should work`() {
        validateTimeComparator {
            val array = it.split(":")
            val hour = array[0].toInt()
            val minute = array[1].toInt()
            String.format("%02d%02d", hour, minute).toInt()
        }
    }
}
