package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.SpinnerHandler
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CustomMealRecyclerViewModel
import java.math.RoundingMode

const val GRAM_OUNCE_RATIO = 0.035274

class AddCustomMealFragment : Fragment() {
    lateinit var viewModel: CustomMealRecyclerViewModel
    private lateinit var ingredientsSpinnerHandler: SpinnerHandler
    private lateinit var mealsSpinnerHandler: SpinnerHandler
    private lateinit var cuisinesSpinnerHandler: SpinnerHandler

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = CustomMealRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel = ViewModelProvider(rootActivity, factory)[
                CustomMealRecyclerViewModel::class.java
        ]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.add_custom_meal, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        unitSpinnerSetup()
        fragmentElementsSetup(view)
    }

    /**
     * Setups all the other elements inside the fragment
     */
    private fun fragmentElementsSetup(view: View) {
        val currentActivity = requireActivity()
        val customMealName = view.findViewById<EditText>(R.id.meal_name)
        val customMealPortion = view.findViewById<EditText>(R.id.meal_portion_quantity)
        val customMealUnitSpinner = view.findViewById<Spinner>(R.id.custom_meal_units_spinner)
        val customMealCarbsAmount = view.findViewById<EditText>(R.id.carbs_amount)
        val customImageUrl = view.findViewById<EditText>(R.id.custom_meal_image_url)
        val createButton = view.findViewById<Button>(R.id.create_custom_meal_button)

        cuisinesSpinnerHandler = SpinnerHandler(
            currentActivity,
            R.id.custom_meal_cuisines_spinner
        )
        ingredientsSpinnerHandler = SpinnerHandler(
            currentActivity,
            R.id.custom_meal_ingredients_spinner
        )
        mealsSpinnerHandler = SpinnerHandler(
            currentActivity,
            R.id.custom_meal_meals_spinner
        )

        createButton.setOnClickListener {
            val anyFieldBlank =
                listOf(
                    customMealName,
                    customMealPortion,
                    customMealCarbsAmount
                ).any { it.text.isBlank() }

            if (!anyFieldBlank) {

                viewModel.addCustomMeal(
                    MealInfo(
                        dbId = DbMealInfoEntity.DEFAULT_DB_ID,
                        dbRestaurantId = DbMealInfoEntity.DEFAULT_DB_ID,
                        restaurantSubmissionId = null,
                        submissionId = MealInfo.DEFAULT_SUBMISSION_ID,
                        name = customMealName.text.toString(),
                        carbs = customMealCarbsAmount.text.toString().toInt(),
                        amount = customMealPortion.text.toString().toInt(),
                        unit = customMealUnitSpinner.selectedItem.toString(),
                        votes = null,
                        isFavorite = false,
                        imageUri = customImageUrl.text?.toString()?.let { Uri.parse(it) },
                        creationDate = TimestampWithTimeZone.now(),
                        //TODO create meal with ingredient input from user
                        ingredientComponents = emptyList(),
                        //TODO create meal with meals input from user
                        mealComponents = emptyList(),
                        //TODO create meal with cuisines input from user
                        cuisines = emptyList(),
                        //Custom meal does not have portions
                        portions = emptyList(),
                        isSuggested = false,
                        source = Source.CUSTOM
                    )
                ).setOnPostExecute {
                    parentFragmentManager.popBackStack()
                }.execute()
            }
        }
    }

    /**
     * Setups the measurement units spinner following the shared preferences.
     */
    @Suppress("UNCHECKED_CAST")
    private fun unitSpinnerSetup() {
        val spinner = view?.findViewById<Spinner>(R.id.custom_meal_units_spinner)
        val spinnerAdapter: ArrayAdapter<String> = spinner!!.adapter as ArrayAdapter<String>

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)
        val defaultUnitKey =
            sharedPreferences.getString("weight_units", "grams")

        val spinnerPosition = spinnerAdapter.getPosition(defaultUnitKey)
        spinner.setSelection(spinnerPosition)

        // Changes the weight value according to the unit spinner
        val customMealPortion = view?.findViewById<EditText>(R.id.meal_portion_quantity)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                throw NotImplementedError("This function is not implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val value = spinner.selectedItem.toString()

                if (customMealPortion!!.text.isNotBlank()) {
                    if (value == "ounces") {
                        customMealPortion.text = convertToOunces(customMealPortion.text.toString())
                    } else {
                        customMealPortion.text = convertToGrams(customMealPortion.text.toString())
                    }
                }

            }
        }
    }

    private fun convertToGrams(currentValue: String): Editable? =
        SpannableStringBuilder(
            (currentValue.toDouble() / GRAM_OUNCE_RATIO)
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_UP)
                .toFloat()
                .toString()
        )

    private fun convertToOunces(currentValue: String): Editable? =
        SpannableStringBuilder(
            (currentValue.toDouble() * GRAM_OUNCE_RATIO)
                .toBigDecimal()
                .setScale(2, RoundingMode.HALF_UP)
                .toFloat()
                .toString()
        )
}