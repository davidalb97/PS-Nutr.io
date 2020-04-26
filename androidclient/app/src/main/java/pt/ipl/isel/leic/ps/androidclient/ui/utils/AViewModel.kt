package pt.ipl.isel.leic.ps.androidclient.ui.utils

import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.repositories.HttpServerRepository

data class LiveInfo<T>(var list: MutableList<T>, var requestPending: Boolean = false)

abstract class AViewModel<T>(val app: NutrioApp) : AndroidViewModel(app) {

    val liveInfo: MutableLiveData<LiveInfo<T>> = MutableLiveData()
    val repo: HttpServerRepository = app.httpServerRepository

    //private lateinit var currentQuery: Iterable<Pair<"TODO", String>>
    lateinit var adapter: ListAdapter<T>

    fun successFunction(): (MutableList<T>) -> Unit = {
        liveInfo.value = LiveInfo(it, false)
        if (it.isEmpty()) {
            Toast.makeText(
                app.applicationContext, R.string.no_result_found,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun errorFunction(): () -> Unit = {
        liveInfo.value = LiveInfo(emptyList<T>().toMutableList(), false)
        Toast.makeText(app.applicationContext, R.string.error_network, Toast.LENGTH_LONG)
            .show()
    }

    fun getMoreItems() {
        // Get the new query and make the new request with
        // the skip - Pagination
    }

    private fun moreItemsSuccessFunction(): (List<T>) -> Unit {
        return {
            liveInfo.value!!.list.addAll(it)
            adapter.notifyDataSetChanged()
            if (it.isEmpty())
                Toast.makeText(
                    getApplication(), R.string.no_more_results_found,
                    Toast.LENGTH_LONG
                ).show()
        }
    }
}