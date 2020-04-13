package pt.ipl.isel.leic.ps.androidclient.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.R

class HomeFragment() : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private val buttonNames = listOf(
        "Restaurants",
        "Meals"
    )

    private val buttonImages = listOf(
        R.drawable.ic_restaurant,
        R.drawable.ic_menu_book
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
        val gridView = view.findViewById<GridView>(R.id.homeGrid)
        gridView.adapter = HomeGridAdapter(view.context, buttonImages, buttonNames)
    }
}
