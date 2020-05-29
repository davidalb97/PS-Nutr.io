package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel
import java.time.LocalTime

class CalculatorFragment : Fragment() {

    lateinit var viewModel: InsulinProfilesRecyclerViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var glucoseObjective: Int? = 0
        var insulinSensitivity: Int? = 0
        var carboRatio: Int? = 0
        var receivedMeal: Any?

        val bundle: Bundle? = this.arguments
        if (bundle != null) {
            receivedMeal = bundle.getParcelable("bundledMeal")!!
        }

        getActualProfile {
            if (it == null) return@getActualProfile
            glucoseObjective = it.glucose_objective
            insulinSensitivity = it.glucose_amount
            carboRatio = it.carbohydrate_amount
        }

        val addButton = view.findViewById<ImageButton>(R.id.meal_add_button)
        val calculateButton = view.findViewById<Button>(R.id.calculate_button)

        addButton.setOnClickListener {
            view.findNavController().navigate(R.id.nav_add_meal_to_calculator)
        }

        calculateButton.setOnClickListener {
            showResult()
        }

    }

    /**
     * Searchs for a profile that matches the current time and returns it
     */
    @SuppressLint("NewApi")
    private fun getActualProfile(cb: (InsulinProfileDto?) -> Unit) {

        val time = LocalTime.now()

        NutrioApp.insulinProfilesRepository.getAllProfiles()
            .observe(this.viewLifecycleOwner, Observer { profiles ->
                if (profiles.isEmpty()) {
                    cb(null)
                    Toast.makeText(
                        this.context,
                        "Please setup a profile before proceed",
                        Toast.LENGTH_LONG
                    ).show()
                    return@Observer
                }
                profiles?.forEach { savedProfile ->
                    val parsedSavedStartTime =
                        LocalTime.parse(savedProfile.start_time)
                    val parsedSavedEndTime =
                        LocalTime.parse(savedProfile.end_time)

                    if (parsedSavedStartTime.isAfter(time) && parsedSavedEndTime.isBefore(time))
                        cb(savedProfile)
                }
            })
    }

    private fun showResult() {

    }

}