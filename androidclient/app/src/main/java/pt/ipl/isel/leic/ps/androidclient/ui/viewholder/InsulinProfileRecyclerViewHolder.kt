package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbInsulinProfileDto

class InsulinProfileRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ARecyclerViewHolder<DbInsulinProfileDto>(view, ctx),
    IDeletable<DbInsulinProfileDto> {

    override lateinit var onDelete: (DbInsulinProfileDto) -> Unit

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

    override fun bindTo(item: DbInsulinProfileDto) {
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
        turnInvisible()
    }
    override fun onLongClick(v: View?): Boolean {
        turnVisible()
        deleteButton.setOnClickListener {
            onDelete(this.item)
            Toast.makeText(
                ctx,
                ctx.getString(R.string.DialogAlert_deleted), Toast.LENGTH_SHORT
            ).show()
            turnInvisible()
            this.bindingAdapter?.notifyItemRemoved(layoutPosition)
        }
        return true
    }

    override fun turnVisible() {
        deleteButton.visibility = View.VISIBLE
    }

    override fun turnInvisible() {
        deleteButton.visibility = View.INVISIBLE
    }

}