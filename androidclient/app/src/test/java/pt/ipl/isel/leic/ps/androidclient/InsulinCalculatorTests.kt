package pt.ipl.isel.leic.ps.androidclient

import org.junit.Assert
import org.junit.Test
import pt.ipl.isel.leic.ps.androidclient.data.db.InsulinCalculator
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone

class InsulinCalculatorTests {

    private val insulinCalculator = InsulinCalculator()

    @Test
    fun insulinCalculatorTest1() {

        // A insulin profile mock
        val insulinProfileMock =
            InsulinProfile(
                "ProfileTest1",
                "00:00",
                "23:59",
                110F,
                50F,
                25F,
                TimestampWithTimeZone.now()
            )

        val currentBloodGlucose = 151F
        val mealCarbsMock = 15F

        Assert.assertEquals(
            1.42f, // expected 1.42 insulin doses to be injected
            insulinCalculator.calculateMealInsulin(
                insulinProfileMock,
                currentBloodGlucose,
                mealCarbsMock
            )
        )
    }

    @Test
    fun insulinCalculatorTest2() {
        // A insulin profile mock
        val insulinProfileMock =
            InsulinProfile(
                "ProfileTest1",
                "00:00",
                "23:59",
                100F,
                30F,
                15F,
                TimestampWithTimeZone.now()
            )

        val currentBloodGlucose = 70F
        val mealCarbsMock = 40F

        Assert.assertEquals(
            1.67f, // expected 1.67 insulin doses to be injected
            insulinCalculator.calculateMealInsulin(
                insulinProfileMock,
                currentBloodGlucose,
                mealCarbsMock
            )
        )
    }
}