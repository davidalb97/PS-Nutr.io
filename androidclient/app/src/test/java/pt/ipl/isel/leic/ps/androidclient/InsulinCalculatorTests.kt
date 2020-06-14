package pt.ipl.isel.leic.ps.androidclient

import org.junit.Assert
import org.junit.Test
import pt.ipl.isel.leic.ps.androidclient.data.db.InsulinCalculator
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.InsulinProfileEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile

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
                110,
                50,
                25
            )

        val currentBloodGlucose = 151
        val mealCarbsMock = 15

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
                100,
                30,
                15
            )

        val currentBloodGlucose = 70
        val mealCarbsMock = 40

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