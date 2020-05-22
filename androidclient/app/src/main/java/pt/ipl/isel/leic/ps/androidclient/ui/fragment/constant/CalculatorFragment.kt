package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.InsulinProfile
import java.time.LocalTime
import java.util.*

class CalculatorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calculator_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var glucoseObjective: Int? = 0
        var insulinSensitivity: Int? = 0
        var carboRatio: Int? = 0

        getActualProfile {
            if (it == null) return@getActualProfile
            glucoseObjective = it.glucose_objective
            insulinSensitivity = it.glucose_amount
            carboRatio = it.carbohydrate_amount
        }

    }

    @SuppressLint("NewApi")
    private fun getActualProfile(cb: (InsulinProfile?) -> Unit) {

        val time = LocalTime.now()

        NutrioApp.insulinProfilesRepository.getAllProfiles()
            .observe(this.viewLifecycleOwner, Observer { profiles ->
                if (profiles.isEmpty()) {
                    cb(null)
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

}