package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IDeleteActionButton
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.util.putItemActions
import pt.ipl.isel.leic.ps.androidclient.ui.util.putNavigation

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
    override var deleteButton: ImageButton = view.findViewById(R.id.delete_item_action)
    override val pressActionView: RelativeLayout = view.findViewById(R.id.actions_layout)

    override fun bindTo(item: InsulinProfile) {
        super.bindTo(item)

        setupTextFields()
        super.setupPressAction(view)
        super.setupOnDeleteAction(bindingAdapter, layoutPosition)
    }

    fun setupTextFields() {
        profileName.text = item.profileName
        val resources = ctx.resources
        startTime.text = String.format(resources.getString(R.string.start_time), item.startTime)
        endTime.text = String.format(resources.getString(R.string.end_time), item.endTime)
        glucoseObjective.text = String.format(
            resources.getString(R.string.glucose_objective_card),
            item.glucoseObjective
        )
        insulinSensitivityFactor.text = String.format(
            resources.getString(R.string.insulin_sensitivity_factor),
            item.glucoseAmountPerInsulin,
            resources.getString(R.string.insulin_unit)
        )
        carboRatio.text = String.format(
            resources.getString(R.string.carbohydrate_ratio),
            item.carbsAmountPerInsulin,
            resources.getString(R.string.insulin_unit)
        )
    }

    override fun onSendToDestination(bundle: Bundle) {
        bundle.putItemActions(actions)
        bundle.putNavigation(navDestination)
    }
}