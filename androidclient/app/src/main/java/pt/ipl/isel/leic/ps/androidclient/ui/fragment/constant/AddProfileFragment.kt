package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbInsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel
import java.time.LocalTime
import java.util.*

class AddProfileFragment : Fragment() {

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
        return inflater.inflate(R.layout.add_insulin_profile_fragment, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileName = view.findViewById<EditText>(R.id.profile_name)
        val glucoseObjective = view.findViewById<EditText>(R.id.glucose_objective)
        val glucoseAmount = view.findViewById<EditText>(R.id.glucose_amount)
        val carboAmount = view.findViewById<EditText>(R.id.carbo_amount)
        val createButton = view.findViewById<Button>(R.id.create_custom_meal)
        val addStartTime = view.findViewById<Button>(R.id.start_time_label)
        val startTimeUser = view.findViewById<TextView>(R.id.start_time_user)
        val addEndTime = view.findViewById<Button>(R.id.end_time_label)
        val endTimeUser = view.findViewById<TextView>(R.id.end_time_user)

        val calendar = Calendar.getInstance()
        val mHour = calendar.get(Calendar.HOUR_OF_DAY);
        val mMinute = calendar.get(Calendar.MINUTE);

        addStartTime.setOnClickListener {
            setupTimePickerDialog(startTimeUser, mHour, mMinute)
                .show()
        }

        addEndTime.setOnClickListener {
            setupTimePickerDialog(endTimeUser, mHour, mMinute)
                .show()
        }

        // Get all profiles before the new insertion
        viewModel.updateListFromLiveData()

        createButton.setOnClickListener {
            // Checks if any field is blank
            val anyFieldBlank =
                listOf(
                    profileName,
                    startTimeUser,
                    endTimeUser,
                    glucoseObjective,
                    glucoseAmount,
                    carboAmount
                ).any { it.text.isBlank() }

            if (anyFieldBlank) {
                Toast.makeText(
                    this.context,
                    getString(R.string.Fill_in_all_the_available_fields),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Creates profile if everything is ok until here
            val profile =
                DbInsulinProfileDto(
                    profileName.text.toString(),
                    startTimeUser.text.toString(),
                    endTimeUser.text.toString(),
                    glucoseObjective.text.toString().toInt(),
                    glucoseAmount.text.toString().toInt(),
                    carboAmount.text.toString().toInt()
                )

            // Asserts time period with the other profiles
            profileTimesValidation(profile) { isValid ->
                if (!isValid) {
                    Toast.makeText(
                        this.context,
                        getString(R.string.This_time_period_is_already_occupied),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.addInsulinProfile(profile)
                    view.findNavController().navigate(R.id.nav_profile)
                }
            }
        }

    }

    /**
     * Checks if the time period passed to the time pickers is valid
     */
    @SuppressLint("NewApi")
    private fun profileTimesValidation(
        profileDb: DbInsulinProfileDto,
        cb: (Boolean) -> Unit
    ) {
        val parsedStartTime = LocalTime.parse(profileDb.start_time)
        val parsedEndTime = LocalTime.parse(profileDb.end_time)

        // Checks if start time is before end time
        if (parsedEndTime.isBefore(parsedStartTime)) {
            Toast.makeText(
                this.context,
                "The time period is not valid",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Checks if it is not overlapping other time periods
        val insulinProfiles = viewModel.mediatorLiveData.value
        var isValid = true
        if (insulinProfiles!!.isNotEmpty()) {
            insulinProfiles.forEach { savedProfile ->
                val parsedSavedStartTime =
                    LocalTime.parse(savedProfile.start_time)
                val parsedSavedEndTime =
                    LocalTime.parse(savedProfile.end_time)

                if (!(parsedEndTime.isBefore(parsedSavedStartTime) ||
                            parsedStartTime.isAfter(parsedSavedEndTime))
                ) {
                    isValid = false
                }
            }
        }
        cb(isValid)
    }

    /**
     * Setups each time picker dialog saving the chosen values to a TextView
     * so it can also display to the user
     */
    @SuppressLint("SetTextI18n")
    private fun setupTimePickerDialog(textView: TextView, hour: Int, minute: Int)
            : TimePickerDialog =
        TimePickerDialog(
            view?.context,
            TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hour: Int, minute: Int ->
                textView.text = String.format("%02d:%02d", hour, minute)
            },
            hour,
            minute,
            true
        )
}