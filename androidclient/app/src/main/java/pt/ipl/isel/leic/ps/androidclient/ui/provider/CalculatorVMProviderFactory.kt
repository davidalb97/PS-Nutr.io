package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealInfoViewModel

const val CALCULATOR_VIEW_STATE: String = "CALCULATOR_VIEW_STATE"
const val BUNDLE_MEAL_INFO = "BUNDLE_MEAL_INFO"
const val BUNDLE_MEAL_DB_ID = "BUNDLE_MEAL_DB_ID"
const val BUNDLE_MEAL_SUBMISSION_ID = "BUNDLE_MEAL_DB_SUBMISSION_ID"
const val BUNDLE_MEAL_SOURCE = "BUNDLE_MEAL_SOURCE_ORDINAL"

class CalculatorVMProviderFactory(
    savedInstanceState: Bundle?,
    intent: Intent,
    private val arguments: Bundle?
) : AViewModelProviderFactory<MealInfoViewModel>(
    savedInstanceState,
    intent
) {
    override fun getStateName(): String = CALCULATOR_VIEW_STATE

    override fun getViewModelClass(): Class<MealInfoViewModel> =
        MealInfoViewModel::class.java

    override fun newViewModel(): MealInfoViewModel = MealInfoViewModel()
}