package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Portion
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.ui.provider.CustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.CustomMealRecyclerViewModel

class AddCustomMealFragment : Fragment() {

    lateinit var viewModel: CustomMealRecyclerViewModel

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = CustomMealRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[CustomMealRecyclerViewModel::class.java]
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

        val customMealName = view.findViewById<EditText>(R.id.meal_name)
        val customMealPortion = view.findViewById<EditText>(R.id.meal_portion_quantity)
        val customMealUnitSpinner = view.findViewById<Spinner>(R.id.custom_meal_units_spinner)
        val customMealCarbsAmount = view.findViewById<EditText>(R.id.carbs_amount)
        val customImageUrl = view.findViewById<EditText>(R.id.custom_meal_image_url)
        val createButton = view.findViewById<Button>(R.id.create_custom_meal_button)


        createButton.setOnClickListener {
            val anyFieldBlank =
                listOf(
                    customMealName,
                    customMealPortion,
                    customMealCarbsAmount
                ).any { it.text.isBlank() }

            if (!anyFieldBlank) {

                val timeZoneWithTimeStamp: String = TimestampWithTimeZone.now().toString()
                viewModel.addCustomMeal(
                    MealInfo(
                        dbId = DbMealInfoEntity.DEFAULT_DB_ID,
                        dbRestaurantId = DbMealInfoEntity.DEFAULT_DB_ID,
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
                        portions = emptyList(),
                        isSuggested = false
                    )
                ).execute()
            }
        }
    }
}