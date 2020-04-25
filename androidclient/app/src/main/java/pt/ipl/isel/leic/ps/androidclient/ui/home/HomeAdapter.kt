package pt.ipl.isel.leic.ps.androidclient.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R

/**
 * Each home section
 */
class SectionHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
    private val sectionName = view.findViewById<TextView>(R.id.sectionName)


    private val buttons : List<RelativeLayout> = listOf(
        view.findViewById(R.id.category_button1),
        view.findViewById(R.id.category_button2)
    )
    private val buttonsContent: Map<ImageView, TextView> = mapOf(
        Pair(
            view.findViewById(R.id.category_image1),
            view.findViewById(R.id.category_name1)
        ),
        Pair(
            view.findViewById(R.id.category_image2),
            view.findViewById(R.id.category_name2)
        )
    )

    fun bindTo(
        sectionName: String,
        sectionButtons: Map<String, Int>,
        buttonsDestinations: List<Int>
    ) {

        val providedNames = sectionButtons.keys.toList()
        val providedImages = sectionButtons.values.toList()
        val images = buttonsContent.keys.toList()
        val names = buttonsContent.values.toList()

        this.sectionName.text = sectionName
        for (i in 0..1) {
            names[i].text = providedNames[i]
            images[i].setImageResource(providedImages[i])
            setupOnClickListener(buttons[i], buttonsDestinations[i])
        }
    }

    private fun setupOnClickListener(button: RelativeLayout, destinationFragment: Int) {
        button.setOnClickListener { view ->
            view.findNavController().navigate(destinationFragment)
        }
    }
}


class HomeAdapter(
    private val sectionsNames: List<String>,
    private val sectionsButtons: List<Map<String, Int>>,
    private val buttonsDestinations: List<List<Int>>
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
            sectionsButtons[position],
            buttonsDestinations[position]
        )
    }
}