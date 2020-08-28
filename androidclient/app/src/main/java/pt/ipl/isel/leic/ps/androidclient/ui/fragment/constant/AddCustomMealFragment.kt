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
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import kotlinx.android.synthetic.main.profile_fragment.*
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.sharedPreferences
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.CustomMeal
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.CuisinePickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.recycler.pick.IngredientPickRecyclerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.adapter.spinner.pick.CuisinePickSpinnerAdapter
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment
import pt.ipl.isel.leic.ps.androidclient.ui.provider.AddCustomMealRecyclerVMProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.WeightUnits
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.AddCustomMealViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.BaseItemPickerViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.CuisinePickViewModel
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.IngredientPickViewModel


class AddCustomMealFragment : BaseFragment() {

    //All data
    private lateinit var viewModel: AddCustomMealViewModel

    //Meals
    private val mealsRecyclerViewId: Int = R.id.custom_meal_meals_list
    private val ingredientsViewModel: IngredientPickViewModel by navGraphViewModels(R.id.nav_custom_meal_nested)
    private lateinit var mealsImgButton: ImageButton
    private val mealsRecyclerAdapter by lazy {
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
    private lateinit var mealCarbs: TextView
    private lateinit var customImageUrl: EditText
    private lateinit var createButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = buildViewModel(savedInstanceState, AddCustomMealViewModel::class.java)
        cuisinesViewModel = buildViewModel(savedInstanceState, CuisinePickViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentElementsSetup(view)
    }

    override fun onResume() {
        super.onResume()

        if(ingredientsViewModel.ingredientsChanged) {
            ingredientsViewModel.ingredientsChanged = false
            mealsRecyclerAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Setups all the other elements inside the fragment
     */
    private fun fragmentElementsSetup(view: View) {

        setupUnitSpinner(view)
        setupIngredients(view)
        setupCuisines(view)
        setupSubmit(view)

        customMealName = view.findViewById(R.id.meal_name)
        customMealName.setText(viewModel.editMeal?.name)

        customMealAmount = view.findViewById(R.id.meal_portion_quantity)
        customMealAmount.setText(viewModel.editMeal?.amount?.toString())

        mealCarbs = view.findViewById(R.id.meal_carbs)
        val totalCarbs = ingredientsViewModel.pickedItems.sumBy { it.carbs } +
                (viewModel.editMeal?.carbs ?: 0)
//        val totalAmount: Float = ingredientsViewModel.pickedItems.fold(0.0F) { sum, it ->
//            sum + it.unit.convert(, it.amount)
//        }

        mealCarbs.text = String.format(
            getString(R.string.custom_meal_total_carbohydrates_amount),
            totalCarbs
        )

        customImageUrl = view.findViewById(R.id.custom_meal_image_url)
        customImageUrl.setText(viewModel.editMeal?.imageUri?.toString())
    }

    private fun setupIngredients(view: View) {

        initRecyclerView(
            view = view,
            recyclerViewId = mealsRecyclerViewId,
            adapter = mealsRecyclerAdapter,
            pickerViewModel = ingredientsViewModel
        )
        mealsImgButton = view.findViewById(R.id.add_custom_meal_add_meal_ingredient)
        mealsImgButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putNavigation(Navigation.BACK_TO_CUSTOM_MEAL)
            bundle.putParentNavigation(Navigation.SEND_TO_ADD_CUSTOM_MEAL)
            view.findNavController().navigate(Navigation.SEND_TO_PICK_CUSTOM_MEAL_INGREDIENTS.navId, bundle)
        }
        if (!ingredientsViewModel.tryRestore()) {
            val editMeal = viewModel.editMeal
            when {
                viewModel.addedIngredients != null -> {
                    ingredientsViewModel.pickedLiveDataHandler.set(viewModel.addedIngredients!!)
                    viewModel.addedIngredients = null
                }
                editMeal != null -> {
                    val ingredients = editMeal.mealComponents.plus(editMeal.ingredientComponents)
                    ingredientsViewModel.pickedLiveDataHandler.set(ingredients)
                }
                else -> ingredientsViewModel.update()
            }
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
                val cuisines = viewModel.editMeal!!.cuisines
                cuisinesViewModel.tryRestore()
                cuisinesViewModel.pickedLiveDataHandler.add(cuisines)
            }
            else -> cuisinesViewModel.update()
        }
    }

    private fun setupSubmit(view: View) {
        createButton = view.findViewById(R.id.create_custom_meal_button)
        createButton.setOnClickListener {
            val noneFieldBlank =
                listOf(
                    customMealName,
                    customMealAmount
                ).none { it.text.isBlank() }

            val noneEmpty = listOf(
                cuisinesViewModel,
                ingredientsViewModel
            ).none { it.pickedItems.isEmpty() }

            if (noneFieldBlank && noneEmpty) {
                val customMeal = getCurrentCustomMeal()
                val editMeal = viewModel.editMeal
                if (editMeal == null) {
                    viewModel.addCustomMeal(customMeal = customMeal, error = log::e)
                } else {
                    viewModel.editCustomMeal(
                        submission = requireNotNull(editMeal.submissionId),
                        customMeal = customMeal,
                        error = log::e
                    )
                }
            }
        }
    }

    private fun <M : Parcelable> initRecyclerView(
        view: View,
        @IdRes recyclerViewId: Int,
        adapter: RecyclerView.Adapter<*>,
        pickerViewModel: BaseItemPickerViewModel<M>
    ) {
        val recyclerView: RecyclerView = view.findViewById(recyclerViewId)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = FlexboxLayoutManager(requireActivity())
        pickerViewModel.observePicked(this) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun <M : Parcelable> initSpinner(
        view: View,
        @IdRes spinnerId: Int,
        adapter: ArrayAdapter<M>,
        pickerViewModel: BaseItemPickerViewModel<M>
    ) {
        val spinner: Spinner = view.findViewById(spinnerId)
        spinner.adapter = adapter
        pickerViewModel.observeRemaining(this) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun getCurrentCustomMeal(): CustomMeal {
        return CustomMeal(
            dbId = viewModel.editMeal?.dbId,
            submissionId = viewModel.editMeal?.submissionId,
            name = customMealName.text.toString(),
            carbs = ingredientsViewModel.items.sumBy { it.carbs },
            //TODO fix amout to be above sum of ingredients ammount
            amount = customMealAmount.text.toString().toFloat(),
            unit = WeightUnits.fromValue(customMealUnitSpinner.selectedItem.toString()),
            imageUri = customImageUrl.text?.toString()?.let { Uri.parse(it) },
            ingredientComponents = ingredientsViewModel.items.filter { !it.isMeal },
            mealComponents = ingredientsViewModel.items.filter { it.isMeal },
            cuisines = cuisinesViewModel.pickedItems
        )
    }

    /**
     * Setups the measurement units spinner following the shared preferences.
     */
    @Suppress("UNCHECKED_CAST")
    private fun setupUnitSpinner(view: View) {
        val spinner = view.findViewById<Spinner>(R.id.custom_meal_units_spinner)
        val spinnerAdapter: ArrayAdapter<String> = spinner!!.adapter as ArrayAdapter<String>

        val defaultUnitKey = sharedPreferences.getWeightUnitOrDefault()

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
                    val newUnit = WeightUnits.fromValue(spinner.selectedItem.toString())
                    val oldUnit = when (newUnit) {
                        WeightUnits.GRAMS -> WeightUnits.OUNCES
                        WeightUnits.OUNCES -> WeightUnits.GRAMS
                    }
                    val currentValue = customMealPortion.text.toString().toFloat()
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