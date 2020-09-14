package pt.ipl.isel.leic.ps.androidclient.ui.util.prompt

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.modular.unit.IWeightUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

private const val MAX_WEIGHT_GRAMS = 1000F
private val MAX_WEIGHT_OUNCES = WeightUnits.GRAMS.convert(WeightUnits.OUNCES, MAX_WEIGHT_GRAMS)

class MealAmountSelector(
    val ctx: Context,
    layoutInflater: LayoutInflater,
    val baseCarbs: Float,
    val baseAmount: Float,
    val startUnit: WeightUnits,
    hideCarbs: Boolean = false,
    onOk: (amount: Float, carbs: Float, unit: WeightUnits) -> Unit
) : IWeightUnitSpinner {

    private val logger = Logger(MealAmountSelector::class)

    override lateinit var currentWeightUnit: WeightUnits
    override val weightUnitSpinnerId: Int = R.id.meal_amount_selector_spinner
    override lateinit var weightUnitSpinner: Spinner
    override lateinit var previousWeightUnit: WeightUnits

    val maxAmount = when (startUnit) {
        WeightUnits.GRAMS -> MAX_WEIGHT_GRAMS
        WeightUnits.OUNCES -> MAX_WEIGHT_OUNCES
    }
    var seekBar: SeekBar
    var carbsTextView: TextView
    var amountTextView: TextView
    var conversionPercentage: Float? = null

    init {

        val alertDialogView = layoutInflater.inflate(R.layout.meal_amount_selector, null)

        carbsTextView = alertDialogView.findViewById(R.id.meal_amount_selector_meal_carbs)
        if (hideCarbs) {
            carbsTextView.visibility = View.GONE
        }
        amountTextView = alertDialogView.findViewById(R.id.meal_amount_selector_meal_amount)
        seekBar = alertDialogView.findViewById(R.id.meal_amount_selector_seekBar)

        setupWeightUnitSpinner(alertDialogView, ctx, startUnit)
        seekBar.max = 100
        conversionPercentage = amountToPercentage(startUnit, baseAmount)
        logger.v("Starting progress: $conversionPercentage%")
        seekBar.progress = conversionPercentage!!.toInt()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateTextViews()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                conversionPercentage = null
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        AlertDialog.Builder(ctx)
            .setIcon(R.drawable.ic_calculate)
            .setTitle(ctx.getString(R.string.select_meal_amount))
            .setView(alertDialogView)
            // Button OK
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()

                val currentAmount = getCurrentAmount()
                val convertedCarbs = getConvertedCarbs(toMealUnit(currentAmount))
                onOk(currentAmount, convertedCarbs, currentWeightUnit)
            }
            .create()
            .show()
    }

    /**
     * Get selected percentage or conversionPercentage
     * (If there was a conversion without [seekBar.setProgress()] call).
     */
    private fun getPercentage(): Float {
        return conversionPercentage ?: seekBar.progress.toFloat()
    }

    /**
     * Gets the current amount according to the [baseAmount] and [seekBar.getProgress()].
     */
    private fun getCurrentAmount(): Float {
        return percentageToAmount(currentWeightUnit)
    }

    /**
     * Converts an amount to the start unit [startUnit]
     * @param currentAmount Amount converted to [currentWeightUnit].
     */
    private fun toMealUnit(currentAmount: Float): Float {
        return currentWeightUnit.convert(startUnit, currentAmount)
    }

    /**
     * Gets carbohydrates relative to [baseCarbs], [baseAmount] and [amount].
     * @param amount Amount converted to [startUnit].
     */
    private fun getConvertedCarbs(amount: Float): Float {
        return ((amount * baseCarbs) / baseAmount)
    }

    private fun updateTextViews() {
        val currentAmount = getCurrentAmount()

        carbsTextView.text = String.format(
            ctx.getString(R.string.meal_carbs_amout),
            getConvertedCarbs(toMealUnit(currentAmount))
        )
        amountTextView.text = String.format(
            ctx.getString(R.string.meal_amout),
            currentAmount
        )
    }

    override fun onWeightUnitChange(converter: (Float) -> Float) {
        val oldValue = percentageToAmount(previousWeightUnit)
        val convertedValue = converter(oldValue)
        logger.v("Converting from $oldValue $previousWeightUnit to $convertedValue $currentWeightUnit")
        conversionPercentage = amountToPercentage(currentWeightUnit, convertedValue)
        updateTextViews()
    }

    /**
     * Converts the [amount] to a percentage relative to [baseAmount].
     * @param unit The [amount] [WeightUnits].
     * @param amount The amount.
     */
    private fun amountToPercentage(unit: WeightUnits, amount: Float): Float {
        val maxAmount = startUnit.convert(unit, maxAmount)
        return (amount * 100) / maxAmount
    }

    /**
     * Gets the amount from the percentage relative to [baseAmount] and converts to target [unit].
     * @param unit Target [WeightUnits] to convert.
     */
    private fun percentageToAmount(unit: WeightUnits): Float {
        val maxAmount = startUnit.convert(unit, maxAmount)
        return ((getPercentage() * maxAmount) / 100)
    }
}

