package pt.ipl.isel.leic.ps.androidclient.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pt.ipl.isel.leic.ps.androidclient.R

class HomeFragment() : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private val sectionsInfo: Map<String, Map<String, Int>> = mapOf(
        Pair(
            "By location",
            mapOf(
                Pair("Restaurants", R.drawable.ic_restaurant),
                Pair("Meals", R.drawable.ic_menu_book)
            )
        ),
        Pair(
            "By Name",
            mapOf(
                Pair("Cuisines", R.drawable.ic_cuisines),
                Pair("Test", R.drawable.ic_favorite)
            )
        )
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeView = view.findViewById<RecyclerView>(R.id.homeLayout)

        val sectionNames = sectionsInfo.keys.toList()
        val sectionsButtons = sectionsInfo.values.toList()

        val homeList =
            this.activity?.findViewById(R.id.homeLayout) as RecyclerView
        homeList.setHasFixedSize(true)
        homeList.layoutManager = LinearLayoutManager(this.requireContext())

        homeView.adapter = HomeAdapter(view.context, sectionNames, sectionsButtons)
    }
}
