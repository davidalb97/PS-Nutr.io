package pt.ipl.isel.leic.ps.androidclient.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton

/**
 * A generic Adapter for Grid Views
 */
abstract class AGridAdapter(
    val images: MutableList<ImageButton>,
    val ctx: Context
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val imageButton: ImageButton =
            if (convertView == null) {
                ImageButton(ctx)
            } else {
                convertView as ImageButton
            }

        return images[position]
    }

    override fun getItem(position: Int): Any {
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return images.size
    }


}