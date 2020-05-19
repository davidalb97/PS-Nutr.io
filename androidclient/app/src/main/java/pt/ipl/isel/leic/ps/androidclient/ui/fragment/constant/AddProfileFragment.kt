package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.insulinProfilesRepository
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker
import java.util.*

class AddProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_insulin_profile_fragment, container, false)
    }

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

        addEndTime.setOnClickListener{
            setupTimePickerDialog(endTimeUser, mHour, mMinute)
                .show()
        }

        createButton.setOnClickListener{

            val profile = InsulinProfile(
                profileName.text.toString(),
                startTimeUser.text.toString(),
                endTimeUser.text.toString(),
                glucoseObjective.text.toString().toInt(),
                glucoseAmount.text.toString().toInt(),
                carboAmount.text.toString().toInt()
            )

            insulinProfilesRepository.addProfile(profile)
        }

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