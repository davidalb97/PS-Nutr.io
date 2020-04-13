package pt.ipl.isel.leic.ps.androidclient.ui.restaurant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import pt.ipl.isel.leic.ps.androidclient.R

class RestaurantFragment : Fragment() {
    private lateinit var restaurantPagerAdapter: RestaurantAdapter
    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_restaurant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        restaurantPagerAdapter = RestaurantAdapter(childFragmentManager)
        //restaurantPagerAdapter.addFragment()
        //restaurantPagerAdapter.addFragment()
        viewPager = view.findViewById(R.id.restaurant_pager)
        viewPager.adapter = restaurantPagerAdapter
    }
}
