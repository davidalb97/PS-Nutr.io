package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class InsulinProfileRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ARecyclerViewHolder<InsulinProfile>(view, ctx),
    IDeletable<InsulinProfile> {

    override lateinit var onDelete: (InsulinProfile) -> AsyncWorker<Unit, Unit>

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
    override var deleteButton: ImageButton =
        view.findViewById(R.id.delete_item_button)

    override fun bindTo(item: InsulinProfile) {
        super.bindTo(item)
        setupTextFields()
        setupListeners()
    }

    @SuppressLint("SetTextI18n")
    fun setupTextFields() {
        profileName.text = item.profileName
        val resources = ctx.resources
        startTime.text =
            resources.getString(R.string.start_time) +
                    " ${item.startTime}"

        endTime.text =
            resources.getString(R.string.end_time) +
                    " ${item.endTime}"

        glucoseObjective.text =
            resources.getString(R.string.glucose_objective_card) +
                    " ${item.glucoseObjective}"

        insulinSensitivityFactor.text =
            resources.getString(R.string.insulin_sensitivity_factor) +
                    " ${item.glucoseAmountPerInsulin} / " +
                    resources.getString(R.string.insulin_unit)

        carboRatio.text =
            resources.getString(R.string.carbohydrate_ratio) +
                    " ${item.carbsAmountPerInsulin} / " +
                    resources.getString(R.string.insulin_unit)
    }

    private fun setupListeners() {
        this.view.setOnClickListener(this)
        this.view.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        setButtonsVisibility(false)
    }

    override fun onLongClick(v: View?): Boolean {
        setButtonsVisibility(true)
        deleteButton.setOnClickListener {
            onDelete(this.item)
                .setOnPostExecute {
                    Toast.makeText(
                        ctx,
                        ctx.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
                    ).show()
                    setButtonsVisibility(false)
                    this.bindingAdapter?.notifyItemRemoved(layoutPosition)
                }.execute()
        }
        return true
    }

    override fun setButtonsVisibility(isVisible: Boolean) {
        val visibility =
            if (isVisible) View.VISIBLE else View.INVISIBLE
        deleteButton.visibility = visibility
    }

}