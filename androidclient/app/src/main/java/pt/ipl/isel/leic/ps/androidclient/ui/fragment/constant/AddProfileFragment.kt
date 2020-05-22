package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.InsulinProfile
import java.time.LocalTime
import java.util.*

class AddProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_insulin_profile_fragment, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val profileName = view.findViewById<EditText>(R.id.profile_name)
        val glucoseObjective = view.findViewById<EditText>(R.id.glucose_objective)
        val glucoseAmount = view.findViewById<EditText>(R.id.profile_name)
        val carboAmount = view.findViewById<EditText>(R.id.carbo_amount)
        val createButton = view.findViewById<Button>(R.id.create_profile)
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

            // Creates profile
            val profile = InsulinProfile(
                profileName.text.toString(),
                startTimeUser.text.toString(),
                endTimeUser.text.toString(),
                glucoseObjective.text.toString().toInt(),
                glucoseAmount.text.toString().toInt(),
                carboAmount.text.toString().toInt()
            )

            // Asserts time period with the other profiles
            isTimePeriodValid(profile) { isValid ->
                if (!isValid) {
                    Toast.makeText(
                        this.context,
                        getString(R.string.This_time_period_is_already_occupied),
                        Toast.LENGTH_LONG
                    ).show()
                    return@isTimePeriodValid
                }
                insulinProfilesRepository.addProfile(profile)
            }
        }
    }

    @SuppressLint("NewApi")
    private fun isTimePeriodValid(
        profile: InsulinProfile,
        consumer: (Boolean) -> Unit
    ) {
        val parsedStartTime = LocalTime.parse(profile.start_time)
        val parsedEndTime = LocalTime.parse(profile.end_time)

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
        insulinProfilesRepository.getAllProfiles()
            .observe(this.viewLifecycleOwner, Observer { profiles ->
                var isValid = true
                if (profiles.isEmpty()) {
                    consumer(isValid)
                    return@Observer
                }
                profiles?.forEach { savedProfile ->
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
                consumer(isValid)
            })
    }

    @SuppressLint("SetTextI18n")
    private fun setupTimePickerDialog(textView: TextView, hour: Int, minute: Int)
            : TimePickerDialog =
        TimePickerDialog(
            view?.context,
            TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, hour: Int, minute: Int ->
                textView.text = "$hour:$minute"
            },
            hour,
            minute,
            true
        )
}