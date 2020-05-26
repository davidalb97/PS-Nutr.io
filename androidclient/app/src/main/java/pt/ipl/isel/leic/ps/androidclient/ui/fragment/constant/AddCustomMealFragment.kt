package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.Meal
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.provider.MealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealRecyclerViewModel

class AddCustomMealFragment : Fragment() {

    lateinit var viewModel: MealRecyclerViewModel

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = MealRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[MealRecyclerViewModel::class.java]
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
        val customMealGlucoseAmount = view.findViewById<EditText>(R.id.glucose_amount)
        val customMealCarbsAmount = view.findViewById<EditText>(R.id.carbs_amount)
        val createButton = view.findViewById<Button>(R.id.create_profile)

        viewModel.updateListFromLiveData()

        val anyFieldBlank =
            listOf(
                customMealName,
                customMealPortion,
                customMealGlucoseAmount,
                customMealCarbsAmount
            ).any { it.text.isBlank() }

        createButton.setOnClickListener {
            if (!anyFieldBlank) {

                /*val customMeal = Meal(

                )

                viewModel.addCustomMeal(customMeal)*/
            }
        }
    }
}