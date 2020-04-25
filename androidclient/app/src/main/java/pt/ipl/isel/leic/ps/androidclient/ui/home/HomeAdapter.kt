package pt.ipl.isel.leic.ps.androidclient.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R

class SectionHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {

    private val section = view.findViewById<CardView>(R.id.section)
    private val sectionName = view.findViewById<TextView>(R.id.sectionName)
    private val category_image1 = view.findViewById<ImageView>(R.id.category_image1)
    private val category_name1 = view.findViewById<TextView>(R.id.category_name1)
    private val category_image2 = view.findViewById<ImageView>(R.id.category_image2)
    private val category_name2 = view.findViewById<TextView>(R.id.category_name2)

    fun bindTo(
        sectionName: String,
        sectionButtons: Map<String, Int>
    ) {

        val names = sectionButtons.keys.toList()

        this.sectionName.text = sectionName
        sectionButtons[names[0]]?.let { this.category_image1.setImageResource(it) }
        this.category_name1.text = names[0]
        sectionButtons[names[1]]?.let { this.category_image2.setImageResource(it) }
        this.category_name2.text = names[1]
    }
}

class HomeAdapter(
    context: Context,
    private val sectionsNames: List<String>,
    private val sectionsButtons: List<Map<String, Int>>
) : RecyclerView.Adapter<SectionHolder>() {

    @SuppressLint("ResourceType")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionHolder =
        SectionHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.grid_section, parent, false) as ViewGroup
        )

    override fun getItemCount(): Int = sectionsNames.size

    override fun onBindViewHolder(holder: SectionHolder, position: Int) {

        holder.bindTo(
            sectionsNames[position],
            sectionsButtons[position]
        )
    }
}