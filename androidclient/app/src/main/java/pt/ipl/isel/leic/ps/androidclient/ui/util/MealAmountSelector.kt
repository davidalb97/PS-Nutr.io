package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IWeightUnitSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

private const val MAX_WEIGHT_GRAMS = 1000

class MealAmountSelector(
    val ctx: Context,
    val layoutInflater: LayoutInflater,
    val baseCarbs: Float,
    val baseAmountGrams: Float,
    val mealUnit: WeightUnits,
    onOk: (amountGrams: Float, carbs: Float) -> Unit
) : IWeightUnitSpinner {

    private val logger = Logger(MealAmountSelector::class)

    override lateinit var currentUnit: WeightUnits
    override lateinit var previousUnit: WeightUnits

    var seekBar: SeekBar
    var carbsTextView: TextView
    var amountTextView: TextView
    var spinner: Spinner
    var conversionPercentage: Float? = null

    init {

        val alertDialogView = layoutInflater.inflate(R.layout.meal_amount_selector, null)

        carbsTextView = alertDialogView.findViewById(R.id.meal_amount_selector_meal_carbs)
        amountTextView = alertDialogView.findViewById(R.id.meal_amount_selector_meal_amount)
        seekBar = alertDialogView.findViewById(R.id.meal_amount_selector_seekBar)
        spinner = alertDialogView.findViewById(R.id.meal_amount_selector_spinner)

        setupWeightUnitSpinner(ctx, spinner)
        seekBar.max = 100
        conversionPercentage = amountToPercentage(WeightUnits.GRAMS, baseAmountGrams)
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

                val convertedAmount = getConvertedAmount(getCurrentAmount())
                val convertedCarbs = getConvertedCarbs(convertedAmount)
                onOk(convertedAmount, convertedCarbs)
            }
            .create()
            .show()
    }

    private fun getCurrentAmount(): Float {
        return percentageToAmount(currentUnit, conversionPercentage ?: seekBar.progress.toFloat())
    }

    private fun getConvertedAmount(currentAmount: Float): Float {
        return currentUnit.convert(mealUnit, currentAmount)
    }

    private fun getConvertedCarbs(amountGrams: Float): Float {
        return ((amountGrams * baseCarbs) / baseAmountGrams)
    }

    private fun updateTextViews() {
        val currentAmount = getCurrentAmount()

        carbsTextView.text = String.format(
            ctx.getString(R.string.meal_carbs_amout),
            getConvertedCarbs(getConvertedAmount(currentAmount))
        )
        amountTextView.text = String.format(
            ctx.getString(R.string.meal_amout),
            currentAmount
        )
    }

    override fun onWeightUnitChange(converter: (Float) -> Float) {
        val oldValue = percentageToAmount(previousUnit, conversionPercentage ?: seekBar.progress.toFloat())
        val convertedValue = converter(oldValue)
        logger.v("Converting from $oldValue $previousUnit to $convertedValue $currentUnit")
        conversionPercentage = amountToPercentage(currentUnit, convertedValue)
        updateTextViews()
    }

    private fun amountToPercentage(unit: WeightUnits, amount: Float): Float {
        val maxAmount = WeightUnits.GRAMS.convert(unit, MAX_WEIGHT_GRAMS.toFloat())
        return (amount * 100) / maxAmount
    }

    private fun percentageToAmount(unit: WeightUnits, percentage: Float): Float {
        val maxAmount = WeightUnits.GRAMS.convert(unit, MAX_WEIGHT_GRAMS.toFloat())
        return ((percentage * maxAmount) / 100)
    }
}

