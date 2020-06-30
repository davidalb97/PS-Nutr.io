package pt.ipl.isel.leic.ps.androidclient.ui.fragment.recycler.request

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
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
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantItem
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.RestaurantRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.listener.ScrollListener
import pt.ipl.isel.leic.ps.androidclient.ui.provider.RestaurantRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.RestaurantRecyclerViewModel

open class RestaurantRecyclerFragment :
    ARequestRecyclerListFragment<RestaurantItem, RestaurantRecyclerViewModel>() {

    protected val adapter: RestaurantRecyclerAdapter by lazy {
        RestaurantRecyclerAdapter(
            viewModel,
            this.requireContext()
        )
    }

    /**
     * ViewModel builder
     * Initializes the view model, calling the respective
     * view model provider factory
     */
    protected fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = RestaurantRecyclerVMProviderFactory(savedInstanceState, rootActivity.intent)
        viewModel =
            ViewModelProvider(rootActivity, factory)[RestaurantRecyclerViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityApp = this.requireActivity().application
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.restaurant_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerList(view)
        setErrorFunction()

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this.requireContext())
        startObserver()

        val searchBar = view.findViewById<SearchView>(R.id.search_restaurant)

        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()) return false
                //viewModel.restaurantName = query //TODO: we should clear this later
                //viewModel.getRestaurantById()
                searchBar.clearFocus()
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean = true

        })
    }

    override fun startScrollListener() {
        list.addOnScrollListener(object :
            ScrollListener(list.layoutManager as LinearLayoutManager, progressWheel) {

            var minimumListSize = 1

            override fun loadMore() {
                minimumListSize = viewModel.items.size + 1
                if (!isLoading && progressBar.visibility == View.INVISIBLE) {
                    startLoading()
                    viewModel.update()
                    stopLoading()
                }
            }

            override fun shouldGetMore(): Boolean =
                !isLoading && minimumListSize < viewModel.items.size
        })
    }

    override fun getRecyclerId() = R.id.itemList

    override fun getProgressBarId() = R.id.progressBar
}