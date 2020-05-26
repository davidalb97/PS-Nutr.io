package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto

class InsulinProfileRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ADeletableRecyclerViewHolder<InsulinProfileDto>(view, ctx) {

    private val profileName: TextView =
        view.findViewById(R.id.insulin_profile_name)
    private val startTime: TextView =
        view.findViewById(R.id.profile_start_time)
    private val endTime: TextView =
        view.findViewById(R.id.profile_end_time)
    private val glucoseObjective: TextView =
        view.findViewById(R.id.profile_glucose_objective_card)
    private val insulinSensitivityFactor: TextView =
        view.findViewById(R.id.profile_fsi_card)
    private val carboRatio: TextView =
        view.findViewById(R.id.profile_carbo_ratio_card)


    override fun bindTo(item: InsulinProfileDto) {
        super.bindTo(item)
        setupTextFields()
        setupListeners()
    }

    @SuppressLint("SetTextI18n")
    fun setupTextFields() {
        profileName.text = item.profile_name
        val resources = ctx.resources
        startTime.text =
            resources.getString(R.string.start_time) +
                    " ${item.start_time}"

        endTime.text =
            resources.getString(R.string.end_time) +
                    " ${item.end_time}"

        glucoseObjective.text =
            resources.getString(R.string.glucose_objective_card) +
                    " ${item.glucose_objective}"

        insulinSensitivityFactor.text =
            resources.getString(R.string.insulin_sensitivity_factor) +
                    " ${item.glucose_amount} / " +
                    resources.getString(R.string.insulin_unit)

        carboRatio.text =
            resources.getString(R.string.carbohydrate_ratio) +
                    " ${item.carbohydrate_amount} / " +
                    resources.getString(R.string.insulin_unit)
    }

    fun setupListeners() {
        this.view.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}