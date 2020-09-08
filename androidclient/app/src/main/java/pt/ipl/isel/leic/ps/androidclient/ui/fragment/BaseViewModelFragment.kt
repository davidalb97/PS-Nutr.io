package pt.ipl.isel.leic.ps.androidclient.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IViewModelManager

abstract class BaseViewModelFragment<VM> :
    BaseFragment(),
    IViewModelManager
        where VM : ViewModel, VM : Parcelable {

    abstract val vmClass: Class<VM>
    lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = buildViewModel(savedInstanceState, vmClass)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun <VM> ViewModelStoreOwner.buildViewModel(savedInstanceState: Bundle?, clazz: Class<VM>): VM
            where VM : ViewModel, VM : Parcelable {
        return buildViewModel(
            intent = requireActivity().intent,
            arguments = arguments,
            savedInstanceState = savedInstanceState,
            vmClass = clazz
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        super.onSaveViewModels(outState, getViewModels())
    }

    open fun getViewModels(): Iterable<Parcelable> = listOf(viewModel)
}