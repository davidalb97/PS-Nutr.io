package pt.ipl.isel.leic.ps.androidclient.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R
import java.lang.RuntimeException

class HomeAdapter(
    context: Context,
    private val buttonImages: List<Int>,
    private val buttonNames: List<String>
) : BaseAdapter() {

    private var inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder", "InflateParams", "ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder = ButtonHolder()
        var view = inflater.inflate(R.layout.category_button, null)

        holder.button = view.findViewById(R.id.category_button)
        holder.image = view.findViewById(R.id.category_image)
        holder.name = view.findViewById(R.id.category_name)

        holder.name.text = buttonNames[position]
        holder.image.setImageResource(buttonImages[position])

        holder.button.setOnClickListener { view ->
            view.findNavController().navigate(getFragment(holder.name.text.toString()))
        }

        return view
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return buttonImages.size
    }

    private fun getFragment(buttonName: String) : Int {
        return when(buttonName) {
            "Restaurants" ->  R.id.nav_restaurant
            "Meals" -> R.id.nav_about // TODO: Change
            else -> throw RuntimeException("Socorro")
        }
    }
}

class ButtonHolder {
    lateinit var button: CardView
    lateinit var image: ImageView
    lateinit var name: TextView
}