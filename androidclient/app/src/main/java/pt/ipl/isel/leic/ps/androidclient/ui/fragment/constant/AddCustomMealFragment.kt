package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.IdRes
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentMealEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbMealInfoEntity
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.MealIngredient
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.CuisinePickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.IngredientPickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.MealInfoPickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick.CuisinePickSpinnerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick.IngredientPickSpinnerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddCustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddCustomMealViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.IngredientPickViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.ItemPickerViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.MealInfoPickViewModel
import pt.ipl.isel.leic.ps.androidclient.util.TimestampWithTimeZone


class AddCustomMealFragment : BaseFragment() {

    //All data
    private lateinit var viewModel: AddCustomMealViewModel

    //Meals
    private val mealsRecyclerViewId: Int = R.id.custom_meal_meals_list
    private lateinit var mealsViewModel: MealInfoPickViewModel
    private lateinit var mealsImgButton: ImageButton
    private val mealsRecyclerAdapter by lazy {
        MealInfoPickRecyclerAdapter(mealsViewModel, requireContext())
    }

    //Ingredients
    private val ingredientsRecyclerViewId: Int = R.id.custom_meal_ingredients_list
    private val ingredientsSpinnerId: Int = R.id.custom_meal_ingredients_spinner
    private lateinit var ingredientsViewModel: IngredientPickViewModel
    private val ingredientsSpinnerAdapter by lazy {
        IngredientPickSpinnerAdapter(ingredientsViewModel, requireContext())
    }
    private val ingredientsRecyclerAdapter: IngredientPickRecyclerAdapter by lazy {
        IngredientPickRecyclerAdapter(ingredientsViewModel, requireContext())
    }

    //Cuisines
    private val cuisinesRecyclerViewId: Int = R.id.custom_meal_cuisines_list
    private val cuisinesSpinnerId: Int = R.id.custom_meal_cuisines_spinner
    private lateinit var cuisinesViewModel: CuisinePickViewModel
    private val cuisinesSpinnerAdapter by lazy {
        CuisinePickSpinnerAdapter(cuisinesViewModel, requireContext())
    }
    private val cuisinesRecyclerAdapter by lazy {
        CuisinePickRecyclerAdapter(cuisinesViewModel, requireContext())
    }

    private lateinit var customMealName: EditText
    private lateinit var customMealAmount: EditText
    private lateinit var customMealUnitSpinner: Spinner
    private lateinit var customMealCarbsAmount: EditText
    private lateinit var customImageUrl: EditText
    private lateinit var createButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = buildViewModel(savedInstanceState, AddCustomMealViewModel::class.java)
        viewModel.addedIngredient = arguments?.getMealInfo()
        mealsViewModel = buildViewModel(savedInstanceState, MealInfoPickViewModel::class.java)
        ingredientsViewModel =
            buildViewModel(savedInstanceState, IngredientPickViewModel::class.java)
        cuisinesViewModel = buildViewModel(savedInstanceState, CuisinePickViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentElementsSetup(view)
    }

    /**
     * Setups all the other elements inside the fragment
     */
    private fun fragmentElementsSetup(view: View) {
        customMealName = view.findViewById(R.id.meal_name)
        customMealName.setText(viewModel.editMeal?.name)

        customMealAmount = view.findViewById(R.id.meal_portion_quantity)
        customMealAmount.setText(viewModel.editMeal?.amount?.toString())

        customMealCarbsAmount = view.findViewById(R.id.carbs_amount)
        customMealCarbsAmount.setText(viewModel.editMeal?.carbs?.toString())

        customImageUrl = view.findViewById(R.id.custom_meal_image_url)
        createButton = view.findViewById(R.id.create_custom_meal_button)

        setupUnitSpinner(view)
        setupMeals(view)
        setupIngredients(view)
        setupCuisines(view)
        setupSubmit()
    }

    private fun setupMeals(view: View) {

        initRecyclerView(
            view = view,
            recyclerViewId = mealsRecyclerViewId,
            adapter = mealsRecyclerAdapter,
            pickerViewModel = mealsViewModel
        )
        mealsImgButton = view.findViewById(R.id.add_custom_meal_add_meal_ingredient)
        mealsImgButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putNavigation(Navigation.SEND_TO_ADD_CUSTOM_MEAL)
            bundle.putItemActions()
            bundle.putSource(Source.CUSTOM)
            view.findNavController().navigate(R.id.nav_tab_meals, bundle)
        }
        when {
            viewModel.editMeal?.mealComponents != null -> {
                val meals = viewModel.editMeal!!.mealComponents
                mealsViewModel.tryRestore()
                mealsViewModel.pickedLiveDataHandler.add(meals)
            }
            viewModel.addedIngredient != null -> {
                mealsViewModel.tryRestore()
                mealsViewModel.pick(viewModel.addedIngredient!!)
                viewModel.addedIngredient = null
            }
            else -> mealsViewModel.update()
        }
    }

    private fun setupIngredients(view: View) {

        initRecyclerView(
            view = view,
            recyclerViewId = ingredientsRecyclerViewId,
            adapter = ingredientsRecyclerAdapter,
            pickerViewModel = ingredientsViewModel
        )
        initSpinner(
            view = view,
            spinnerId = ingredientsSpinnerId,
            adapter = ingredientsSpinnerAdapter,
            pickerViewModel = ingredientsViewModel
        )
        when {
            viewModel.editMeal?.ingredientComponents != null -> {
                val ingredients = viewModel.editMeal!!.ingredientComponents
                ingredientsViewModel.tryRestore()
                ingredientsViewModel.pickedLiveDataHandler.add(ingredients)
            }
            else -> ingredientsViewModel.update()
        }
    }

    private fun setupCuisines(view: View) {

        initRecyclerView(
            view = view,
            recyclerViewId = cuisinesRecyclerViewId,
            adapter = cuisinesRecyclerAdapter,
            pickerViewModel = cuisinesViewModel
        )
        initSpinner(
            view = view,
            spinnerId = cuisinesSpinnerId,
            adapter = cuisinesSpinnerAdapter,
            pickerViewModel = cuisinesViewModel
        )
        when {
            viewModel.editMeal?.cuisines != null -> {
                val ingredients = viewModel.editMeal!!.cuisines
                cuisinesViewModel.tryRestore()
                cuisinesViewModel.pickedLiveDataHandler.add(ingredients)
            }
            else -> ingredientsViewModel.update()
        }
    }

    private fun setupSubmit() {
        createButton.setOnClickListener {
            val noneFieldBlank =
                listOf(
                    customMealName,
                    customMealAmount,
                    customMealCarbsAmount
                ).none { it.text.isBlank() }

            val noneEmpty = listOf(
                cuisinesViewModel,
                ingredientsViewModel,
                mealsViewModel
            ).none { it.pickedItems.isEmpty() }

            if (noneFieldBlank && noneEmpty) {

                viewModel.addCustomMeal(
                    getMealInfo()
                ).setOnPostExecute {
                    parentFragmentManager.popBackStack()
                }.execute()
            }
        }
    }

    private fun <M : Parcelable> initRecyclerView(
        view: View,
        @IdRes recyclerViewId: Int,
        adapter: RecyclerView.Adapter<*>,
        pickerViewModel: ItemPickerViewModel<M>
    ) {
        val recyclerView: RecyclerView = view.findViewById(recyclerViewId)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = FlexboxLayoutManager(requireActivity())
        pickerViewModel.observeRemaining(this) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun <M : Parcelable> initSpinner(
        view: View,
        @IdRes spinnerId: Int,
        adapter: ArrayAdapter<M>,
        pickerViewModel: ItemPickerViewModel<M>
    ) {
        val spinner: Spinner = view.findViewById(spinnerId)
        spinner.adapter = adapter
        pickerViewModel.observeRemaining(this) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun getMealInfo() = MealInfo(
        dbId = DbMealInfoEntity.DEFAULT_DB_ID,
        dbRestaurantId = DbMealInfoEntity.DEFAULT_DB_ID,
        restaurantSubmissionId = null,
        //Custom meal does not have submissionId
        submissionId = MealInfo.DEFAULT_SUBMISSION_ID,
        name = customMealName.text.toString(),
        carbs = customMealCarbsAmount.text.toString().toInt(),
        amount = customMealAmount.text.toString().toInt(),
        unit = customMealUnitSpinner.selectedItem.toString(),
        votes = null,
        isFavorite = false,
        imageUri = customImageUrl.text?.toString()?.let { Uri.parse(it) },
        creationDate = TimestampWithTimeZone.now(),
        ingredientComponents = ingredientsViewModel.pickedItems,
        mealComponents = mealsViewModel.pickedItems.map { mealComponent ->
            MealIngredient(
                dbMealId = DbMealInfoEntity.DEFAULT_DB_ID,
                isMeal = true,
                dbId = DbComponentMealEntity.DEFAULT_DB_ID,
                submissionId = mealComponent.submissionId,
                name = mealComponent.name,
                carbs = mealComponent.carbs,
                amount = mealComponent.amount,
                unit = mealComponent.unit,
                imageUri = mealComponent.imageUri,
                source = mealComponent.source
            )
        },
        cuisines = cuisinesViewModel.pickedItems,
        //Custom meal does not have portions
        portions = emptyList(),
        isSuggested = false,
        source = Source.CUSTOM
    )

    /**
     * Setups the measurement units spinner following the shared preferences.
     */
    @Suppress("UNCHECKED_CAST")
    private fun setupUnitSpinner(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.custom_meal_units_spinner)
        val spinnerAdapter: ArrayAdapter<String> = spinner!!.adapter as ArrayAdapter<String>

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)

        val defaultUnitKey = viewModel.unit ?: sharedPreferences.getUnit()

        val spinnerPosition = spinnerAdapter.getPosition(defaultUnitKey)
        spinner.setSelection(spinnerPosition)

        // Changes the weight value according to the unit spinner
        val customMealPortion = view.findViewById<EditText>(R.id.meal_portion_quantity)
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                throw NotImplementedError("This function is not implemented")
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                if (customMealPortion!!.text.isNotBlank()) {
                    val newUnit = WeightUnits.valueOf(spinner.selectedItem.toString())
                    val oldUnit = when (newUnit) {
                        WeightUnits.GRAMS -> WeightUnits.OUNCES
                        WeightUnits.OUNCES -> WeightUnits.GRAMS
                    }
                    val currentValue = customMealPortion.text.toString().toDouble()
                    customMealPortion.setText(oldUnit.convert(newUnit, currentValue).toString())
                }
            }
        }
    }

    override fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): AddCustomMealRecyclerVMProviderFactory {
        return AddCustomMealRecyclerVMProviderFactory(
            arguments,
            savedInstanceState,
            intent
        )
    }

    override fun getLayout() = R.layout.add_custom_meal

    override fun onSaveInstanceState(outState: Bundle) {
        /*
        viewModel.name = customMealName.text.toString()
        viewModel.carbs = customMealCarbsAmount.text.toString().toInt()
        viewModel.amount = customMealPortion.text.toString().toInt()
        viewModel.unit = customMealUnitSpinner.selectedItem.toString()
        viewModel.imageUri = customImageUrl.text?.toString()?.let { Uri.parse(it) }
        viewModel.ingredientComponents = ingredientsViewModel.pickedItems
        viewModel.mealComponents = mealsViewModel.pickedItems.map { mealComponent ->
            MealIngredient(
                dbMealId = DbMealInfoEntity.DEFAULT_DB_ID,
                isMeal = true,
                dbId = DbComponentMealEntity.DEFAULT_DB_ID,
                submissionId = mealComponent.submissionId,
                name = mealComponent.name,
                carbs = mealComponent.carbs,
                amount = mealComponent.amount,
                unit = mealComponent.unit,
                imageUri = mealComponent.imageUri,
                source = mealComponent.source
            )
        }
        viewModel.cuisines = cuisinesViewModel.pickedItems
        outState.putParcelable(AddCustomMealViewModel::class.simpleName, viewModel)

         */
    }
}