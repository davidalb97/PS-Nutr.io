package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.InsulinCalculator
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.closeKeyboard
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel
import java.time.LocalTime

const val BUNDLED_MEAL_TAG = "bundledMeal"

class CalculatorFragment : Fragment() {

    lateinit var viewModel: InsulinProfilesRecyclerViewModel

    private var receivedMeal: CustomMealDto? = null
    private var currentProfile: InsulinProfileDto? = null
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
        searchForBundledMeal()
        setupAddMealButtonListener()
        setupCalculateButtonListener()
    }

    /**
     * Checks if the user already selected a meal for the calculation.
     * This meal will be present inside the fragment bundle if it was
     * already selected.
     */
    private fun searchForBundledMeal() {
        val bundle: Bundle? = this.arguments
        if (bundle != null) {
            receivedMeal = bundle.getParcelable(BUNDLED_MEAL_TAG)!!
            addBundledMealHolder()
        }
    }

    /**
     * Setups the CardView in the fragment that will hold the chosen
     * meal.
     */
    private fun addBundledMealHolder() {
        val selectedMealCard =
            view?.findViewById<CardView>(R.id.calc_meal_card)
        val selectedMealName =
            view?.findViewById<TextView>(R.id.calc_meal_name)
        val selectedMealDeleteButton =
            view?.findViewById<ImageButton>(R.id.remove_meal_from_calc_button)

        selectedMealName?.text = receivedMeal?.name
        selectedMealCard?.visibility = View.VISIBLE

        selectedMealDeleteButton?.setOnClickListener { view ->
            cleanBundledMeal(selectedMealCard)
        }
    }

    /**
     * Setups the button that allows the user to select meals.
     */
    private fun setupAddMealButtonListener() {
        val addButton =
            view?.findViewById<ImageButton>(R.id.meal_add_button)
        addButton?.setOnClickListener {
            view?.findNavController()?.navigate(R.id.nav_add_meal_to_calculator)
        }
    }

    /**
     * Setups the calculation button behaviour.
     */
    private fun setupCalculateButtonListener() {
        val calculateButton =
            view?.findViewById<Button>(R.id.calculate_button)

        calculateButton?.setOnClickListener {
            closeKeyboard(this.requireActivity())
            observeExistingInsulinProfiles()
            // Gets the profiles, unplugging the observer later
            viewModel.updateListFromLiveData()
        }
    }

    /**
     * Observes modifications in the LiveData
     */
    private fun observeExistingInsulinProfiles() {
        viewModel.observe(this) { profilesList ->
            if (profilesList.isEmpty())
                showNoExistingProfilesToast()
            else
                getProfileAndCalculateResult()
        }
    }

    /**
     * Pops up when there are no insulin profiles created
     */
    private fun showNoExistingProfilesToast() {
        Toast.makeText(
            this.context,
            "Please setup a profile before proceed",
            Toast.LENGTH_LONG
        ).show()
    }

    /**
     * Gets a valid profile based on the current LocalTime;
     * Gets the bundled meal, saved in this fragment field;
     * Calculates the insulin dosage that needs to be injected.
     */
    private fun getProfileAndCalculateResult() {
        val currentBloodGlucose =
            view?.findViewById<EditText>(R.id.user_blood_glucose)
        getActualProfile { profile -> currentProfile = profile!! }
        val result: Float
        if (receivedMeal != null && currentProfile != null) {
            result =
                calculator.calculateMealInsulin(
                    currentProfile!!,
                    currentBloodGlucose!!.text.toString().toInt(),
                    receivedMeal!!.carboAmount
                )
            showResultDialog(result)

            // Clean fields after show result
            cleanCurrentGlucose(currentBloodGlucose)
            cleanBundledMeal()
        }
    }

    /**
     * Searches for a profile that matches the current time
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


    /**
     * Shows the result dialog window
     */
    private fun showResultDialog(result: Float) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Results")
        builder.setMessage("You need to inject $result insulin doses")
        builder.setPositiveButton(view?.context?.getString(R.string.Dialog_Ok)) { _, _ -> }
        builder.create().show()
    }

    /**
     * Cleans the current blood glucose text field
     */
    private fun cleanCurrentGlucose(textBox: EditText? = null) {
        if (textBox != null) {
            textBox.text = null
            textBox.clearFocus()
        } else {
            val currentBloodGlucose =
                view?.findViewById<EditText>(R.id.user_blood_glucose)
            currentBloodGlucose?.text = null
            currentBloodGlucose?.clearFocus()
        }
    }

    /**
     * Cleans the bundled meal and the fragment meal holder
     */
    private fun cleanBundledMeal(card: CardView? = null) {
        if (card != null) {
            card.visibility = View.INVISIBLE
        } else {
            val selectedMealCard: CardView? =
                view?.findViewById(R.id.calc_meal_card)
            selectedMealCard?.visibility = View.INVISIBLE
        }
        receivedMeal = null
        this.arguments?.remove(BUNDLED_MEAL_TAG)
    }
}