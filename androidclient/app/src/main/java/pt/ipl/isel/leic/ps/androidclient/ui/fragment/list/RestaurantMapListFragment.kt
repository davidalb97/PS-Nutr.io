package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.NonNull
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R

private const val DEFAULT_INTERVAL_IN_MILLISECONDS: Long = 100000L
private const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5

class MapFragment : RestaurantListFragment(), OnMapReadyCallback {

    private var mapView: MapView? = null
    private var locationEngine: LocationEngine? = null
    private val callback = initLocationEngineCallback()
    private var mapboxMap: MapboxMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Mapbox.getInstance(requireActivity().application, getString(R.string.access_token))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Bypass super's onLocationEnabled()
        //super.onViewCreated(view, savedInstanceState)
        super.initRecyclerView(view)
        mapView = view.findViewById(R.id.mapBoxView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        val searchBar = view.findViewById<SearchView>(R.id.search_restaurant)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false
                //viewModel.restaurantName = query //TODO Use query for restaurant name filter
                //viewModel.getRestaurantById()
                searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean = true

        })
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        mapboxMap.setStyle(Style.TRAFFIC_DAY) { style: Style ->
            enableLocationComponent(style)
        }
    }

    /**
     * Initialize the Maps SDK's LocationComponent
     */
    @SuppressLint("MissingPermission")
    private fun enableLocationComponent(@NonNull loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            // Get an instance of the component
            val locationComponent: LocationComponent = mapboxMap!!.locationComponent

            if (!locationComponent.isLocationComponentActivated) {
                // Set the LocationComponent activation options
                val locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(requireContext(), loadedMapStyle)
                        .useDefaultLocationEngine(false)
                        .build()

                // Activate with the LocationComponentActivationOptions object
                locationComponent.activateLocationComponent(locationComponentActivationOptions)

                // Enable to make component visible
                locationComponent.isLocationComponentEnabled = true

                // Set the component's camera mode
                locationComponent.cameraMode = CameraMode.TRACKING

                // Set the component's render mode
                locationComponent.renderMode = RenderMode.COMPASS
                initLocationEngine()
            }

        } else {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_PERMISSIONS_CODE
            )
        }
    }

    /**
     * Set up the LocationEngine and the parameters for querying the device's location
     */
    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(requireActivity())
        val request =
            LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()
        locationEngine!!.requestLocationUpdates(request, callback, getMainLooper())
        locationEngine!!.getLastLocation(callback)
    }

    override fun onLocationEnabled() {
        mapboxMap?.setStyle(Style.TRAFFIC_DAY) { style: Style ->
            enableLocationComponent(style)
        }
    }

    private fun initLocationEngineCallback(): LocationEngineCallback<LocationEngineResult> {
        return object : LocationEngineCallback<LocationEngineResult> {

            /**
             * The LocationEngineCallback interface's method which fires when the device's location has changed.
             *
             * @param result the LocationEngineResult object which has the last known location within it.
             */
            override fun onSuccess(result: LocationEngineResult?) {

                val location = result?.lastLocation ?: return

                // Pass the new location to the Maps SDK's LocationComponent
                mapboxMap?.locationComponent?.forceLocationUpdate(location)

                //Update viewmodel with new location
                recyclerViewModel.latitude = location.latitude
                recyclerViewModel.longitude = location.longitude
                recyclerViewModel.update()
            }

            /**
             * The LocationEngineCallback interface's method which fires when the device's location can't be captured
             *
             * @param exception the exception message
             */
            override fun onFailure(exception: java.lang.Exception) {
                Toast.makeText(
                    app,
                    exception.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    /**
     * Methods to get the geolocation and MapBox working,
     * including its permissions.
     */

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar

    override fun getLayout() = R.layout.map_fragment

    override fun getNoItemsLabelId() = R.id.no_restaurants_found
}