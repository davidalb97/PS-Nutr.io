package pt.ipl.isel.leic.ps.androidclient.ui.fragment.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.LATITUDE_VAR
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.LONGITUDE_VAR
import pt.ipl.isel.leic.ps.androidclient.data.model.Restaurant
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.RestaurantRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request.ARequestRecyclerListFragment
import pt.ipl.isel.leic.ps.androidclient.ui.listener.ScrollListener
import pt.ipl.isel.leic.ps.androidclient.ui.provider.RestaurantRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.RestaurantRecyclerViewModel


class MapFragment
    : ARequestRecyclerListFragment<Restaurant, RestaurantRecyclerViewModel>(),
    OnMapReadyCallback,
    PermissionsListener {

    private val adapter: RestaurantRecyclerAdapter by lazy {
        RestaurantRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    private val DEFAULT_INTERVAL_IN_MILLISECONDS: Long = 100000L
    private val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5
    private var mapboxMap: MapboxMap? = null
    private var mapView: MapView? = null
    private var permissionsManager: PermissionsManager? = null
    private var locationEngine: LocationEngine? = null
    private lateinit var callback: LocationEngineCallback<LocationEngineResult>

    val PERMISSION_ID = 42

    /**
     * ViewModel builder
     * Initializes the view model, calling the respective
     * view model provider factory
     */
    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = RestaurantRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[RestaurantRecyclerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityApp = this.requireActivity().application
        buildViewModel(savedInstanceState)
        Mapbox.getInstance(activityApp, getString(R.string.access_token))
        return inflater.inflate(R.layout.map_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLocationEngineCallback()
        mapView = view.findViewById(R.id.mapBoxView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this);

        initRecyclerList(view)
        setCallbackFunctions()
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()

        val searchBar = view.findViewById<SearchView>(R.id.search_restaurant)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false
                searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean = true

        })
    }


    override fun startScrollListener() {
        list.addOnScrollListener(object :
            ScrollListener(list.layoutManager as LinearLayoutManager, progressBar) {

            var minimumListSize = 1

            override fun loadMore() {
                minimumListSize = viewModel.mediatorLiveData.value!!.size + 1
                if (!isLoading && progressBar.visibility == View.INVISIBLE) {
                    startLoading()
                    viewModel.updateListFromLiveData()
                    stopLoading()
                }
            }

            override fun shouldGetMore(): Boolean =
                !isLoading && minimumListSize < viewModel.mediatorLiveData.value!!.size
        })
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
        if (locationEngine != null) {
            locationEngine!!.removeLocationUpdates(callback);
        }
        mapView?.onDestroy();
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapboxMap = mapboxMap

        mapboxMap.setStyle(
            Style.TRAFFIC_NIGHT
        ) { style -> enableLocationComponent(style) }
    }

    private fun enableLocationComponent(loadedMapStyle: Style) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {

            // Get an instance of the component
            val locationComponent = mapboxMap!!.locationComponent

            // Set the LocationComponent activation options
            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(activityApp, loadedMapStyle)
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
        } else {
            permissionsManager = PermissionsManager(this)
            permissionsManager!!.requestLocationPermissions(requireActivity())
        }
    }

    @SuppressLint("MissingPermission")
    private fun initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(requireContext())
        val request: LocationEngineRequest =
            LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build()
        locationEngine!!.requestLocationUpdates(request, callback, getMainLooper())
        locationEngine!!.getLastLocation(callback)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionsManager?.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    override fun onExplanationNeeded(permissionsToExplain: MutableList<String>?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            mapboxMap!!.getStyle {
                enableLocationComponent(it)
            }
        } else {
            Toast.makeText(
                activityApp,
                "Location permission not granted",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun initLocationEngineCallback() {
        this.callback = object : LocationEngineCallback<LocationEngineResult> {
            override fun onSuccess(result: LocationEngineResult?) {
                val location = result!!.lastLocation ?: return

                // Create a Toast which displays the new location's coordinates
                viewModel.parameters[LATITUDE_VAR] =
                    result.lastLocation!!.latitude.toString()

                viewModel.parameters[LONGITUDE_VAR] =
                    result.lastLocation!!.longitude.toString()

                // Pass the new location to the Maps SDK's LocationComponent
                if (mapboxMap != null && result.lastLocation != null) {
                    mapboxMap!!.locationComponent
                        .forceLocationUpdate(result.lastLocation)
                }

                viewModel.getNearbyRestaurants()
            }

            override fun onFailure(exception: java.lang.Exception) {
                Toast.makeText(
                    activityApp,
                    exception.localizedMessage,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }
}