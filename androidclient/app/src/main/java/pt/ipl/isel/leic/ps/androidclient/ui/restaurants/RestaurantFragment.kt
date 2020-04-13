package pt.ipl.isel.leic.ps.androidclient.ui.restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.MapFragment
import pt.ipl.isel.leic.ps.androidclient.ui.cuisines.CuisinesFragment

class RestaurantFragment : Fragment() {

    private lateinit var restaurantViewModel: ViewModel
    private lateinit var restaurantPagerAdapter: RestaurantAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var tabs: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        restaurantViewModel =
            ViewModelProvider(this).get(RestaurantViewModel::class.java)
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        restaurantPagerAdapter = RestaurantAdapter(childFragmentManager)
        restaurantPagerAdapter.addFragment(CuisinesFragment(), "Search by Cuisines")
        restaurantPagerAdapter.addFragment(MapFragment(), "Search by Location")
        restaurantPagerAdapter.addFragment(CuisinesFragment(), "Search by Meals")
        viewPager = view.findViewById(R.id.viewPager)
        tabs = view.findViewById(R.id.restaurant_tab)
        viewPager.adapter = restaurantPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }
}
