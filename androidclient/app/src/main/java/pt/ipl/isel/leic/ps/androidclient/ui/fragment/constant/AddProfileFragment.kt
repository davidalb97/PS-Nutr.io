package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.TimestampWithTimeZone
import pt.ipl.isel.leic.ps.androidclient.ui.provider.InsulinProfilesVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.InsulinProfilesRecyclerViewModel
import java.text.SimpleDateFormat
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
        val defaultHour = calendar.get(Calendar.HOUR_OF_DAY)
        val defaultMinute = calendar.get(Calendar.MINUTE)

        addStartTime.setOnClickListener {
            setupTimePickerDialog(startTimeUser, defaultHour, defaultMinute)
                .show()
        }

        addEndTime.setOnClickListener {
            setupTimePickerDialog(endTimeUser, defaultHour, defaultMinute)
                .show()
        }

        // Get all profiles before the new insertion
        viewModel.update()

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
                    app,
                    getString(R.string.Fill_in_all_the_available_fields),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Creates profile if everything is ok until here
            val profile = InsulinProfile(
                profileName.text.toString(),
                startTimeUser.text.toString(),
                endTimeUser.text.toString(),
                glucoseObjective.text.toString().toInt(),
                glucoseAmount.text.toString().toInt(),
                carboAmount.text.toString().toInt(),
                TimestampWithTimeZone.now()
            )

            // Asserts time period with the other profiles
            profileTimesValidation(profile) { isValid ->
                if (!isValid) {
                    Toast.makeText(
                        app,
                        getString(R.string.This_time_period_is_already_occupied),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel
                        .addDbInsulinProfile(profile)
                        .setOnPostExecute { parentFragmentManager.popBackStack() }
                        .execute()
                }
            }
        }

    }

    /**
     * Checks if the time period passed to the time pickers is valid
     */
    private fun profileTimesValidation(
        profileDb: InsulinProfile,
        cb: (Boolean) -> Unit
    ) {
        val timeInstance = SimpleDateFormat("HH:MM")

        val parsedStartTime = timeInstance.parse(profileDb.startTime)!!
        val parsedEndTime = timeInstance.parse(profileDb.endTime)!!

        // Checks if start time is before end time
        if (parsedEndTime.before(parsedStartTime)) {
            Toast.makeText(
                app,
                "The time period is not valid",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        // Checks if it is not overlapping other time periods
        val insulinProfiles = viewModel.items
        var isValid = true
        if (insulinProfiles.isNotEmpty()) {
            insulinProfiles.forEach { savedProfile ->
                val parsedSavedStartTime = timeInstance.parse(savedProfile.startTime)!!
                val parsedSavedEndTime = timeInstance.parse(savedProfile.endTime)!!

                if (!(parsedEndTime.before(parsedSavedStartTime) ||
                            parsedStartTime.after(parsedSavedEndTime))
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
     * @param textView The [TextView] to set
     * @param defaultHour The default time (hours)
     * @param defaultMinute The default time (minutes)
     */
    private fun setupTimePickerDialog(
        textView: TextView,
        defaultHour: Int,
        defaultMinute: Int
    ): TimePickerDialog {
        return TimePickerDialog(
            view?.context,
            TimePickerDialog.OnTimeSetListener { _: TimePicker, hour: Int, minute: Int ->
                textView.text = String.format("%02d:%02d", hour, minute)
            },
            defaultHour,
            defaultMinute,
            true
        )
    }
}