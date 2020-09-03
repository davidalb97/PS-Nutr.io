package pt.ipl.isel.leic.ps.androidclient.ui.modular.pick

import android.os.Parcelable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.BaseItemPickerViewModel

interface IRemainingPickSpinner {

    fun <M : Parcelable> setupSpinner(
        lifecycleOwner: LifecycleOwner,
        view: View,
        @IdRes spinnerId: Int,
        adapter: ArrayAdapter<M>,
        pickerViewModel: BaseItemPickerViewModel<M>
    ) {
        val spinner: Spinner = view.findViewById(spinnerId)
        spinner.adapter = adapter
        pickerViewModel.observeRemaining(lifecycleOwner) {
            adapter.notifyDataSetChanged()
        }
    }
}