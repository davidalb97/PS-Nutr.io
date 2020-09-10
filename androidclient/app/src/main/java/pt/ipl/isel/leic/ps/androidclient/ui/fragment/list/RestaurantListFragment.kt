package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.RestaurantRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.provider.RestaurantRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.RestaurantListViewModel

internal const val REQUEST_PERMISSIONS_CODE = 0

open class RestaurantListFragment : BaseListFragment<
        RestaurantItem,
        RestaurantListViewModel,
        RestaurantRecyclerAdapter
        >(), IUserSession {

    override val paginated = true
    override val recyclerViewId = R.id.itemList
    override val progressBarId = R.id.progressBar
    override val layout = R.layout.restaurant_list
    override val noItemsTextViewId = R.id.no_restaurants_found

    override val vmClass = RestaurantListViewModel::class.java
    override val vMProviderFactorySupplier = ::RestaurantRecyclerVMProviderFactory
    override val recyclerAdapter: RestaurantRecyclerAdapter by lazy {
        RestaurantRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    private lateinit var locationManager: LocationManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Init super's recycler list handler
        super.onViewCreated(view, savedInstanceState)

        val addButton = view.findViewById<ImageButton>(R.id.add_restaurant)

        addButton.setOnClickListener {
            ensureUserSession(requireContext()) {
                view.findNavController().navigate(Navigation.SEND_TO_ADD_RESTAURANT.navId)
            }
        }

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
        viewModel.latitude = latitude
        viewModel.longitude = longitude

        viewModel.setupList()
    }
}