package pt.ipl.isel.leic.ps.androidclient

import org.junit.Assert
import org.junit.Test
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

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
}
