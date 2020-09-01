package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IDeleteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_GLUCOSE_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.GlucoseUnits
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits

abstract class InsulinProfileRecyclerViewHolder(
    override val actions: List<ItemAction>,
    navDestination: Navigation,
    view: View,
    ctx: Context
) : BaseRecyclerViewHolder<InsulinProfile>(
    navDestination = navDestination,
    view = view,
    ctx = ctx
), IDeleteActionButton<InsulinProfile> {

    private val profileName: TextView = view.findViewById(R.id.insulin_profile_name)
    private val startTime: TextView = view.findViewById(R.id.profile_start_time)
    private val endTime: TextView = view.findViewById(R.id.profile_end_time)
    private val glucoseObjective: TextView = view.findViewById(R.id.profile_glucose_objective_card)
    private val insulinSensitivityFactor: TextView = view.findViewById(R.id.profile_fsi_card)
    private val carboRatio: TextView = view.findViewById(R.id.profile_carbo_ratio_card)
    override var deleteButtonId: Int = R.id.delete_item_action
    override lateinit var deleteButton: ImageButton
    override val pressActionViewId: Int = R.id.actions_layout
    override lateinit var pressActionView: RelativeLayout

    override fun bindTo(item: InsulinProfile) {
        super.bindTo(item)
        setupTextFields()
        super.setupPressAction(view)
        super.setupOnDeleteAction(view, bindingAdapter, layoutPosition)
    }

    private fun setupTextFields() {

        val glucoseUnit = sharedPreferences.getGlucoseUnitOrDefault()
        val carbUnit = sharedPreferences.getWeightUnitOrDefault()

        var convertedGlucoseObjective: Float? = null
        var convertedGlucoseAmount: Float? = null
        var convertedCarbAmount: Float? = null

        val glucoseUnitTarget = GlucoseUnits.fromValue(glucoseUnit)
        val weightUnitTarget = WeightUnits.fromValue(carbUnit)

        if (glucoseUnit != DEFAULT_GLUCOSE_UNIT.toString()) {
            convertedGlucoseObjective = DEFAULT_GLUCOSE_UNIT
                .convert(glucoseUnitTarget, item.glucoseObjective)

            convertedGlucoseAmount = DEFAULT_GLUCOSE_UNIT
                .convert(glucoseUnitTarget, item.glucoseAmountPerInsulin)
        }

        if (carbUnit != DEFAULT_WEIGHT_UNIT.toString()) {
            convertedCarbAmount = DEFAULT_WEIGHT_UNIT
                .convert(weightUnitTarget, item.carbsAmountPerInsulin)
        }

        profileName.text = item.profileName
        val resources = ctx.resources
        startTime.text = String.format(resources.getString(R.string.start_time), item.startTime)
        endTime.text = String.format(resources.getString(R.string.end_time), item.endTime)
        glucoseObjective.text = String.format(
            resources.getString(R.string.glucose_objective_card),
            convertedGlucoseObjective ?: item.glucoseObjective,
            sharedPreferences.getGlucoseUnitOrDefault()
        )
        insulinSensitivityFactor.text = String.format(
            resources.getString(R.string.insulin_sensitivity_factor),
            convertedGlucoseAmount ?: item.glucoseAmountPerInsulin,
            sharedPreferences.getGlucoseUnitOrDefault(),
            resources.getString(R.string.insulin_unit)
        )
        carboRatio.text = String.format(
            resources.getString(R.string.carbohydrate_ratio),
            convertedCarbAmount ?: item.carbsAmountPerInsulin,
            sharedPreferences.getWeightUnitOrDefault(),
            resources.getString(R.string.insulin_unit)
        )
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putItemActions(actions)
        bundle.putNavigation(navDestination)
    }
}