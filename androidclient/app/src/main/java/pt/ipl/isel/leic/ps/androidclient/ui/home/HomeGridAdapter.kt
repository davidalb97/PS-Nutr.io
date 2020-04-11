package pt.ipl.isel.leic.ps.androidclient.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R

class HomeGridAdapter(
    private val context: Context?,
    private val buttonImages: List<Int>,
    private val buttonNames: List<String>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var view = View(context)
        val inflater =
            context
                ?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        if (convertView == null) {
            view = inflater.inflate(R.layout.category_button, null)

            val buttonImage = view.findViewById<ImageView>(R.id.category_image)
            val buttonText = view.findViewById<TextView>(R.id.category_name)
            buttonImage?.setImageResource(buttonImages[position])
            buttonText?.text = buttonNames[position]
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return buttonImages.size
    }
}