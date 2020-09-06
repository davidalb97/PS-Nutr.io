package pt.ipl.isel.leic.ps.androidclient.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.app
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.hasInternetConnection
import pt.ipl.isel.leic.ps.androidclient.ui.provider.BaseViewModelProviderFactory
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger

abstract class BaseFragment : Fragment() {

    val log: Logger by lazy { Logger(javaClass) }

    /**
     * Creates or restores a [ViewModel] based of it's class and it's factory [getVMProviderFactory]
     */
    protected open fun <VM> buildViewModel(
        savedInstanceState: Bundle?,
        vmClass: Class<VM>
    ): VM where VM : ViewModel {
        val rootActivity = this.requireActivity()
        val factory = getVMProviderFactory(savedInstanceState, rootActivity.intent)
        return ViewModelProvider(this, factory)[vmClass]
    }

    /**
     * Inflates the layout (with [getLayout])
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayout(), container, false)
    }

    @LayoutRes
    protected abstract fun getLayout(): Int

    protected abstract fun getVMProviderFactory(
        savedInstanceState: Bundle?,
        intent: Intent
    ): BaseViewModelProviderFactory

    protected open fun onError(throwable: Throwable) {
        if (!hasInternetConnection()) {
            Toast.makeText(
                NutrioApp.app,
                R.string.no_internet_connection,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                NutrioApp.app,
                R.string.error_network,
                Toast.LENGTH_SHORT
            ).show()
        }
        log.e(throwable)
    }

    fun popUpBackStack() {
        view?.findNavController()?.popBackStack()
    }
}