package pt.ipl.isel.leic.ps.androidclient.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R

class HomeAdapter(
    context: Context,
    private val buttonImages: List<Int>,
    private val buttonNames: List<String>
) : BaseAdapter() {


    private var inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    @SuppressLint("ViewHolder", "InflateParams", "ResourceType")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder = ButtonsHolder()
        var view = inflater.inflate(R.layout.category_button, null)

        holder.button = view.findViewById(R.id.buttonCardView)
        holder.buttonImage = view.findViewById(R.id.category_image)
        holder.buttonName = view.findViewById(R.id.category_name)

        holder.buttonImage.setImageResource(buttonImages[position])
        holder.buttonName.text = buttonNames[position]

        holder.button.setOnClickListener { view ->
            view.findNavController().navigate(getFragment(holder.buttonName))
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

    fun getFragment(buttonName: TextView) : Int {
        return when(buttonName.text) {
            "Restaurants" ->  R.id.nav_restaurant
            "Meals" -> R.id.nav_about // TODO: Change
            else -> 0
        }
    }
}

class ButtonsHolder {
    lateinit var button: CardView
    lateinit var buttonImage: ImageView
    lateinit var buttonName: TextView
}