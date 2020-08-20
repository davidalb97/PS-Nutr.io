package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.RestaurantRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.provider.RestaurantRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.RestaurantListViewModel

internal const val REQUEST_PERMISSIONS_CODE = 0

open class RestaurantListFragment : BaseListFragment<
        RestaurantItem,
        RestaurantListViewModel,
        RestaurantRecyclerAdapter
        >() {

    private lateinit var locationManager: LocationManager

    override val recyclerAdapter: RestaurantRecyclerAdapter by lazy {
        RestaurantRecyclerAdapter(
            recyclerViewModel,
            this.requireContext()
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val searchBar = view.findViewById<SearchView>(R.id.search_restaurant)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false
                //viewModel.restaurantName = query //TODO: Actually use query text listener
                //viewModel.getRestaurantById()
                searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean = true
        })

        if (isLocationEnabled()) {
            onLocationEnabled()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_CODE
            )
        }
    }

    private fun isLocationEnabled(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    protected open fun onLocationRejected() {
        Toast.makeText(app, R.string.turn_on_geolocation, Toast.LENGTH_LONG)
            .show()
        parentFragmentManager.popBackStack()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS_CODE && grantResults.all { result ->
                result == PackageManager.PERMISSION_GRANTED
            }
        ) {
            onLocationEnabled()
        } else onLocationRejected()
    }

    @SuppressLint("MissingPermission")
    protected open fun onLocationEnabled() {
        locationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!

        val longitude = lastLocation.longitude
        val latitude = lastLocation.latitude
        recyclerViewModel.latitude = latitude
        recyclerViewModel.longitude = longitude

        recyclerViewModel.update()
    }

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar

    override fun getLayout() = R.layout.restaurant_list

    override fun getNoItemsLabelId() = R.id.no_restaurants_found

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): RestaurantRecyclerVMProviderFactory {
        return RestaurantRecyclerVMProviderFactory(arguments, savedInstanceState, intent)
    }

    override fun getRecyclerViewModelClass() = RestaurantListViewModel::class.java
}