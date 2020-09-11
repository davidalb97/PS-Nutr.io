package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BaseViewModelProviderFactory

interface IViewModelManager : ILog {

    var savedInstanceState: Bundle?

    val vMProviderFactorySupplier: (
        arguments: Bundle?,
        savedInstanceState: Bundle?,
        intent: Intent
    ) -> BaseViewModelProviderFactory

    /**
     * Creates or restores a [ViewModel] based of it's class and it's [vMProviderFactorySupplier]
     */
    fun <VM> ViewModelStoreOwner.buildViewModel(
        arguments: Bundle?,
        intent: Intent,
        savedInstanceState: Bundle?,
        vmClass: Class<VM>
    ): VM where VM : ViewModel {
        log.v("Initializing ViewModel (${vmClass.simpleName})")
        val factory = vMProviderFactorySupplier(arguments, savedInstanceState, intent)
        return ViewModelProvider(this, factory)[vmClass]
    }

    fun onSaveViewModels(outState: Bundle, viewModels: Iterable<Parcelable>) {
        log.v("Saving ViewModels (${viewModels.joinToString(",") { 
            it.javaClass.simpleName
        }})")
        viewModels.forEach { viewModel ->
            outState.putParcelable(viewModel.javaClass.simpleName, viewModel)
        }
    }
}