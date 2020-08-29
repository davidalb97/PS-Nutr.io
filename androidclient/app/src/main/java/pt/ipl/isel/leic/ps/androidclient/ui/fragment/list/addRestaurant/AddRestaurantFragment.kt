package pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.addRestaurant

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomRestaurant
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.CuisinePickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick.CuisinePickSpinnerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.REQUEST_PERMISSIONS_CODE
import pt.ipl.isel.leic.ps.androidclient.ui.modular.pick.IPickedFlexBoxRecycler
import pt.ipl.isel.leic.ps.androidclient.ui.modular.pick.IRemainingPickSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddRestaurantVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddRestaurantViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel

class AddRestaurantFragment : BaseFragment(), IRemainingPickSpinner, IPickedFlexBoxRecycler {

    private lateinit var viewModel: AddRestaurantViewModel

    private lateinit var locationManager: LocationManager
    private var latitude: Double? = null
    private var longitude: Double? = null

    //Cuisines
    private val cuisinesRecyclerViewId: Int = R.id.restaurant_cuisines_list
    private val cuisinesSpinnerId: Int = R.id.restaurant_cuisines_spinner
    private lateinit var cuisinesViewModel: CuisinePickViewModel
    private val cuisinesSpinnerAdapter by lazy {
        CuisinePickSpinnerAdapter(cuisinesViewModel, requireContext())
    }
    private val cuisinesRecyclerAdapter by lazy {
        CuisinePickRecyclerAdapter(cuisinesViewModel, requireContext())
    }

    private lateinit var restaurantNameEditText: EditText
    private lateinit var submitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = buildViewModel(savedInstanceState, AddRestaurantViewModel::class.java)
        cuisinesViewModel = buildViewModel(savedInstanceState, CuisinePickViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCuisines(view)
        setupSubmit(view)

        restaurantNameEditText = view.findViewById(R.id.restaurant_name)
        restaurantNameEditText.setText(viewModel.editRestaurant?.name)

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
        Toast.makeText(NutrioApp.app, R.string.turn_on_geolocation, Toast.LENGTH_LONG)
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
        locationManager =
            NutrioApp.app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!

        latitude = lastLocation.latitude
        longitude = lastLocation.longitude
    }

    override fun getLayout(): Int = R.layout.add_restaurant_fragment

    private fun getCurrentCustomRestaurant(): CustomRestaurant {
        return CustomRestaurant(
            dbId = viewModel.editRestaurant?.dbId,
            id = viewModel.editRestaurant?.id,
            name = restaurantNameEditText.text.toString(),
            latitude = latitude?.toFloat()!!,
            longitude = longitude?.toFloat()!!,
            imageUri = viewModel.editRestaurant?.imageUri,
            cuisines = cuisinesViewModel.pickedItems
        )
    }

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): AddRestaurantVMProviderFactory = AddRestaurantVMProviderFactory(
        arguments,
        savedInstanceState,
        intent
    )

    private fun setupCuisines(view: View) {
        setupRecyclerView(
            this,
            ctx = requireContext(),
            view = view,
            recyclerViewId = cuisinesRecyclerViewId,
            adapter = cuisinesRecyclerAdapter,
            pickerViewModel = cuisinesViewModel
        )
        setupSpinner(
            this,
            view = view,
            spinnerId = cuisinesSpinnerId,
            adapter = cuisinesSpinnerAdapter,
            pickerViewModel = cuisinesViewModel
        )
        when {
            viewModel.editRestaurant?.cuisines != null -> {
                val cuisines = viewModel.editRestaurant!!.cuisines
                cuisinesViewModel.tryRestore()
                cuisinesViewModel.pickedLiveDataHandler.add(cuisines)
            }
            else -> cuisinesViewModel.update()
        }
    }

    private fun setupSubmit(view: View) {
        submitButton = view.findViewById(R.id.create_custom_meal_button)
        submitButton.setOnClickListener {
            val noneFieldBlank = restaurantNameEditText.text.isNotBlank()
            val noneEmpty = cuisinesViewModel.pickedItems.isNotEmpty()

            if (noneFieldBlank && noneEmpty) {
                val customRestaurant = getCurrentCustomRestaurant()
                val editRestaurant = viewModel.editRestaurant
                if (editRestaurant == null) {
                    viewModel.addRestaurant(
                        customRestaurant = customRestaurant,
                        onSuccess = {
                            Toast.makeText(
                                app,
                                getString(R.string.created_restaurant),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = log::e
                    )
                }/* else {
                    viewModel.editRestaurant(
                        submission = requireNotNull(editRestaurant.submissionId),
                        customMeal = customMeal,
                        error = log::e
                    )
                }*/
            }
        }
    }

}