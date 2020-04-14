package pt.ipl.isel.leic.ps.androidclient.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pt.ipl.isel.leic.ps.androidclient.R

// TODO: no API Key, use another API besides Google Maps :(
class MapFragment : Fragment(), OnMapReadyCallback {

    lateinit var mapView: MapView
    lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        mapView = view.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.onResume()

        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val lisbon = LatLng(38.7436883, -9.1952225)
        googleMap.addMarker(MarkerOptions().position(lisbon).title("Marker in Lisbon"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lisbon))
    }
}
