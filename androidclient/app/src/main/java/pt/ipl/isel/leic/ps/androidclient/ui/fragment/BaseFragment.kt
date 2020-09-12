package pt.ipl.isel.leic.ps.androidclient.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.hasInternetConnection
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

abstract class BaseFragment : Fragment(), ILog {

    override val log: Logger by lazy { Logger(javaClass) }

    abstract val layout: Int

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log.v("onCreate() called!")
    }

    /**
     * Inflates the layout (with [layout])
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        log.v("onCreateView() called! Inflating...")
        return inflate(inflater, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log.v("onViewCreated() called!")
    }

    protected fun inflate(inflater: LayoutInflater, container: ViewGroup?): View {
        log.v("Inflating...")
        return inflater.inflate(layout, container, false)
    }

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

    fun requireIntent(): Intent {
        return requireActivity().intent
    }

    fun navigate(navigation: Navigation, bundle: Bundle? = null) {
        view?.findNavController()?.navigate(navigation.navId, bundle)
    }

    fun popBackStack() {
        log.v("Popping back stack")
        view?.findNavController()?.popBackStack()
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        log.v("onDetach() called!")
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        log.v("onDestroy() called!")
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        log.v("onDestroyView() called!")
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        log.v("onResume() called!")
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        log.v("onStart() called!")
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        log.v("onStop() called!")
    }
}