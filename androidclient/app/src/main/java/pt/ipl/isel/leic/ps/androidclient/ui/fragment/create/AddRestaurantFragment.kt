package pt.ipl.isel.leic.ps.androidclient.ui.fragment.create

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomRestaurant
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.CuisinePickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick.CuisinePickSpinnerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseViewModelFragment
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.list.REQUEST_PERMISSIONS_CODE
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IRequiredTextInput
import pt.ipl.isel.leic.ps.androidclient.ui.modular.pick.IPickedFlexBoxRecycler
import pt.ipl.isel.leic.ps.androidclient.ui.modular.pick.IRemainingPickSpinner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddRestaurantVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddRestaurantViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel

class AddRestaurantFragment : BaseViewModelFragment<AddRestaurantViewModel>(),
    IRemainingPickSpinner,
    IPickedFlexBoxRecycler,
    IRequiredTextInput {

    override val layout: Int = R.layout.add_restaurant_fragment
    override val vmClass = AddRestaurantViewModel::class.java
    override val vMProviderFactorySupplier = ::AddRestaurantVMProviderFactory

    private lateinit var locationManager: LocationManager
    private var latitude: Double? = null
    private var longitude: Double? = null

    //Cuisines
    private val cuisinesRecyclerViewId: Int = R.id.restaurant_cuisines_list
    private val cuisinesSpinnerId: Int = R.id.restaurant_cuisines_spinner
    private val cuisinesViewModel by lazy {
        buildViewModel(savedInstanceState, CuisinePickViewModel::class.java)
    }
    private val cuisinesSpinnerAdapter by lazy {
        CuisinePickSpinnerAdapter(cuisinesViewModel, requireContext())
    }
    private val cuisinesRecyclerAdapter by lazy {
        CuisinePickRecyclerAdapter(cuisinesViewModel, requireContext())
    }

    private lateinit var restaurantNameEditText: EditText
    private lateinit var submitButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCuisines(view)
        setupSubmit(view)

        restaurantNameEditText = view.findViewById(R.id.restaurant_name)
        restaurantNameEditText.setText(viewModel.name)
        restaurantNameEditText.onFinishEdit {
            viewModel.name = it
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

    private fun onLocationRejected() {
        Toast.makeText(app, R.string.turn_on_geolocation, Toast.LENGTH_LONG).show()
        super.popBackStack()
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
    private fun onLocationEnabled() {
        locationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!

        latitude = lastLocation.latitude
        longitude = lastLocation.longitude
    }

    private fun getCurrentCustomRestaurant(): CustomRestaurant {
        return CustomRestaurant(
            dbId = null,
            id = null,
            name = restaurantNameEditText.text.toString(),
            latitude = latitude?.toFloat()!!,
            longitude = longitude?.toFloat()!!,
            //Server does not support it
            image = null,
            cuisines = cuisinesViewModel.pickedItems
        )
    }

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
        cuisinesViewModel.setupList()
    }

    private fun setupSubmit(view: View) {
        submitButton = view.findViewById(R.id.create_custom_meal_button)
        submitButton.setOnClickListener {
            val pickerIsEmpty = cuisinesViewModel.pickedItems.isEmpty()

            if (pickerIsEmpty) {
                Toast.makeText(
                    app,
                    getString(R.string.select_cuisine),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (validateTextViews(requireContext(), restaurantNameEditText)) {
                viewModel.addRestaurant(
                    customRestaurant = getCurrentCustomRestaurant(),
                    onSuccess = {
                        Toast.makeText(
                            app,
                            getString(R.string.created_restaurant),
                            Toast.LENGTH_SHORT
                        ).show()
                        super.popBackStack()
                    },
                    onError = log::e
                )
            }
        }
    }

    override fun getViewModels(): Iterable<Parcelable> = listOf(viewModel, cuisinesViewModel)
}