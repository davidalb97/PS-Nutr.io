package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger

abstract class AViewModelProviderFactory<VM : Parcelable>(
    val savedInstanceState: Bundle?,
    val intent: Intent
) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val log =
            Logger("${AViewModelProviderFactory::class.java.simpleName}<${getViewModelClass()}>")
        val viewModelClass = getViewModelClass()
        if (savedInstanceState != null) {
            val model: VM? = savedInstanceState.getParcelable(getStateName())
            if (model != null) {
                log.v("Restored ${viewModelClass.simpleName} from Bundle!")
                return model as T
            }
        }
        return when (modelClass) {
            viewModelClass -> newViewModel().apply {
                log.v("Creating ${viewModelClass.simpleName} from the scratch!")
            } as T
            else -> throw IllegalArgumentException("There is no ViewModel for class $modelClass")
        }
    }

    abstract fun getStateName(): String

    abstract fun getViewModelClass(): Class<VM>

    abstract fun newViewModel(): VM
}