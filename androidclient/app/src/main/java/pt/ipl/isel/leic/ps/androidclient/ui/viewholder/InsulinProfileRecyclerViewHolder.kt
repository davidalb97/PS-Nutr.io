package pt.ipl.isel.leic.ps.androidclient.ui.viewholder

import android.content.Context
import android.view.ViewGroup
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.source.model.InsulinProfile

class InsulinProfileRecyclerViewHolder(
    view: ViewGroup,
    ctx: Context
) : ARecyclerViewHolder<InsulinProfile>(view) {

    private val profileName: TextView =
        view.findViewById(R.id.insulin_profile_name)


    override fun bindTo(item: InsulinProfile) {
        super.bindTo(item)
        profileName.text = item.profile_name
    }
}