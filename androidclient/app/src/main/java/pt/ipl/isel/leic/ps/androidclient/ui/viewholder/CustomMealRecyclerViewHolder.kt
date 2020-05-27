package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.CustomMealDto

class CustomMealRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ADeletableRecyclerViewHolder<CustomMealDto>(view, ctx) {

    private val customMealName: TextView =
        view.findViewById(R.id.saved_meal_name)

    override fun bindTo(item: CustomMealDto) {
        super.bindTo(item)
        customMealName.text = item.name
        setupListeners()
    }

    fun setupListeners() {
        this.view.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}