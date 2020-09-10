package pt.ipl.isel.leic.ps.androidclient.ui.fragment

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IViewModelManager

abstract class BaseViewModelFragment<VM> :
    BaseFragment(),
    IViewModelManager
        where VM : ViewModel, VM : Parcelable {

    abstract val vmClass: Class<VM>
    var savedInstanceState: Bundle? = null
    open val viewModel: VM by lazy {
        buildViewModel(savedInstanceState, vmClass)
    }

    init {
        retainInstance = true
    }

    /**
     * Used to save [savedInstanceState] due to [setRetainInstance] disabling onCreate().
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        restoreArguments()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState
        restoreArguments()
    }

    open fun <VM> ViewModelStoreOwner.buildViewModel(savedInstanceState: Bundle?, clazz: Class<VM>): VM
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

        //Save fragment arguments
        outState.putBundle(javaClass.simpleName, arguments)

        //Save fragment ViewModels
        super.onSaveViewModels(outState, getViewModels())
    }

    private fun restoreArguments() {
        val savedInstanceState = savedInstanceState
        if(savedInstanceState != null) {
            arguments = savedInstanceState.getBundle(javaClass.simpleName)
        }
    }

    open fun getViewModels(): Iterable<Parcelable> = listOf(viewModel)
}