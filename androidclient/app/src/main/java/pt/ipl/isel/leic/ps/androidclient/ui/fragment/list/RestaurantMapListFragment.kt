package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list

import android.Manifest
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.navigation.findNavController
import com.mapbox.android.core.location.*
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.location.LocationComponent
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback
import com.mapbox.mapboxsdk.maps.Style
import com.mapbox.mapboxsdk.style.layers.PropertyFactory
import com.mapbox.mapboxsdk.style.layers.SymbolLayer
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

private const val DEFAULT_INTERVAL_IN_MILLISECONDS: Long = 100000L
private const val DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5

private const val STYLE_URI = "mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41"
private const val ICON_ID = "ICON_ID"
private const val SOURCE_ID = "SOURCE_ID"
private const val LAYER_ID = "LAYER_ID"


class MapFragment : RestaurantListFragment(), OnMapReadyCallback {

    override val paginated = true
    override val recyclerViewId = R.id.itemList
    override val progressBarId = R.id.progressBar
    override val layout = R.layout.map_fragment
    override val noItemsTextViewId = R.id.no_restaurants_found

    private var mapView: MapView? = null
    private var locationEngine: LocationEngine? = null
    private val callback = initLocationEngineCallback()
    private var mapBoxMap: MapboxMap? = null
    private var points: MutableList<Feature> = mutableListOf()

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
        observeAndAddPoints()
        mapView = view.findViewById(R.id.mapBoxView)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        val addButton = view.findViewById<ImageButton>(R.id.add_restaurant)

        addButton.setOnClickListener {
            ensureUserSession(requireContext()) {
                navigate(Navigation.SEND_TO_ADD_RESTAURANT)
            }
        }
    }

    override fun onMapReady(mapboxMap: MapboxMap) {
        this.mapBoxMap = mapboxMap

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
            val locationComponent: LocationComponent = mapBoxMap!!.locationComponent

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
        mapBoxMap?.setStyle(Style.TRAFFIC_DAY) { style: Style ->
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
                mapBoxMap?.locationComponent?.forceLocationUpdate(location)

                //Update viewmodel with new location
                viewModel.latitude = location.latitude
                viewModel.longitude = location.longitude

                viewModel.setupList()
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

    private fun observeAndAddPoints() {
        viewModel.observe(this, { restaurantList ->
            restaurantList.forEach {
                points.add(
                    Feature.fromGeometry(
                        Point.fromLngLat(it.longitude.toDouble(), it.latitude.toDouble())
                    )
                )
            }
            mapBoxMap?.setStyle(
                Style.Builder()
                    .fromUri(STYLE_URI)
                    .withImage(
                        ICON_ID, BitmapFactory.decodeResource(
                            resources, R.drawable.mapbox_marker_icon_default
                        )
                    )
                    .withSource(
                        GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(points))
                    )
                    .withLayer(
                        SymbolLayer(LAYER_ID, SOURCE_ID)
                            .withProperties(
                                PropertyFactory.iconImage(ICON_ID),
                                PropertyFactory.iconAllowOverlap(true),
                                PropertyFactory.iconIgnorePlacement(true)
                            )
                    )
            )
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
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }
}