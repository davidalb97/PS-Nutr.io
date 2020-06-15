package pt.ipl.isel.leic.ps.androidclient.ui.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.provider.*
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.MealInfoViewModel

class MealDetailFragment : Fragment(){

    private val log = Logger(MealDetailFragment::class.java)
    lateinit var viewModel: MealInfoViewModel

    private var receivedMeal: MealInfo? = null

    private fun buildViewModel(savedInstanceState: Bundle?) {
        val rootActivity = this.requireActivity()
        val factory = CalculatorVMProviderFactory(savedInstanceState, rootActivity.intent, arguments)
        viewModel = ViewModelProvider(rootActivity, factory)[MealInfoViewModel::class.java]

        //Read passed info from bundle
        viewModel.mealInfo = arguments?.getParcelable<MealInfo>(BUNDLE_MEAL_INFO)
        viewModel.source = arguments?.getInt(BUNDLE_MEAL_SOURCE, -1)?.let {
            if(it == -1) null else Source.values()[it]
        }
        viewModel.submissionId = arguments?.getInt(BUNDLE_MEAL_SUBMISSION_ID, -1)?.let {
            if(it == -1) null else it
        }
        viewModel.dbId = arguments?.getLong(BUNDLE_MEAL_DB_ID, -1)?.let {
            val check: Long = -1
            if(it == check) null else it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        buildViewModel(savedInstanceState)
        return inflater.inflate(R.layout.meal_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setOnErrorFunc(log::e)
        viewModel.observe(this) {
            receivedMeal = it.first()
            setupView()
        }
        viewModel.update()
    }

    private fun setupView() {
        log.v("View created!")
    }
}