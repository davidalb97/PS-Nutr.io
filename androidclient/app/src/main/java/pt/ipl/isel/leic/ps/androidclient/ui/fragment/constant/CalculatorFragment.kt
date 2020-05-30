package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.InsulinCalculator
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel
import java.time.LocalTime

class CalculatorFragment : Fragment() {

    lateinit var viewModel: InsulinProfilesRecyclerViewModel
    private val calculator = InsulinCalculator()

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = InsulinProfilesVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[InsulinProfilesRecyclerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.calculator_fragment, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var receivedMeal: CustomMealDto? = null
        var currentProfile: InsulinProfileDto? = null

        val bundle: Bundle? = this.arguments
        if (bundle != null) {
            receivedMeal = bundle.getParcelable("bundledMeal")!!
        }

        val addButton = view.findViewById<ImageButton>(R.id.meal_add_button)
        val calculateButton = view.findViewById<Button>(R.id.calculate_button)
        val currentBloodGlucose = view.findViewById<EditText>(R.id.user_blood_glucose)

        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.nav_add_meal_to_calculator)
        }

        calculateButton.setOnClickListener {
            var result: Float = 0.0f

            viewModel.observe(this) {
                if (it.isEmpty()) {
                    Toast.makeText(
                        this.context,
                        "Please setup a profile before proceed",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    getActualProfile { profile -> currentProfile = profile }

                    if (receivedMeal != null && currentProfile != null) {
                        result =
                            calculator.calculateMealInsulin(
                                currentProfile!!,
                                currentBloodGlucose.text.toString().toInt(),
                                receivedMeal.carboAmount
                            )
                        showResultDialog(result)
                    }
                }
            }
            viewModel.updateListFromLiveData()
        }
    }

    /**
     * Searchs for a profile that matches the current time and returns it
     */
    @SuppressLint("NewApi")
    private fun getActualProfile(cb: (InsulinProfileDto?) -> Unit) {

        // Gets actual time in hh:mm format
        val time = LocalTime.now()
            .withNano(0)
            .withSecond(0)

        val profiles = viewModel.items
        if (profiles.isEmpty()) {
            cb(null)
            Toast.makeText(
                this.context,
                "Please setup a profile before proceed",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        viewModel.items.forEach { savedProfile ->
            val parsedSavedStartTime =
                LocalTime.parse(savedProfile.start_time)
            val parsedSavedEndTime =
                LocalTime.parse(savedProfile.end_time)
            if (time.isAfter(parsedSavedStartTime) && time.isBefore(parsedSavedEndTime))
                cb(savedProfile)
        }
    }

    private fun showResultDialog(result: Float) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Results")
        builder.setMessage("You need to inject $result insulin doses")
        builder.setPositiveButton(view?.context?.getString(R.string.Dialog_Ok)) { _, _ -> }
        builder.create().show()
    }
}