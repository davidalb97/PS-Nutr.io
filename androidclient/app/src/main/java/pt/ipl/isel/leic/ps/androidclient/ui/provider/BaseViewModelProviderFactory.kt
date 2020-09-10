package pt.ipl.isel.leic.ps.androidclient.ui.provider

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pt.ipl.isel.leic.ps.androidclient.ui.util.Logger

abstract class BaseViewModelProviderFactory(
    val arguments: Bundle?,
    val savedInstanceState: Bundle?,
    val intent: Intent
) : ViewModelProvider.Factory {

    val logger by lazy { Logger(javaClass) }

    abstract fun <T : ViewModel?> newViewModel(modelClass: Class<T>): ViewModel?

    @Suppress("UNCHECKED_CAST")
    override fun <T> create(modelClass: Class<T>): T where T : ViewModel {
        if (savedInstanceState != null) {
            val model: Parcelable? = savedInstanceState.getParcelable(modelClass.simpleName)
            if (model != null) {
                logger.v("Restored ViewModel from Bundle!")
                return model as T
            }
        }
        return newViewModel(modelClass)
            ?.let {
                logger.v("Creating ViewModel from the scratch!")
                it as T
            }
            ?: throw IllegalArgumentException("There is no ViewModel for class $modelClass")
    }
}