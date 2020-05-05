package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.lifecycle.ViewModel

/**
 * A generic Adapter for Grid Views
 */
abstract class AGridAdapter<ViewM : ViewModel>(
    val viewModel: ViewM,
    val ctx: Context
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getItem(position: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

}