package pt.ipl.isel.leic.ps.androidclient.ui.modular.pick

import android.content.Context
import android.os.Parcelable
import android.view.View
import androidx.annotation.IdRes
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import pt.ipl.isel.leic.ps.androidclient.ui.viewmodel.list.pick.BaseItemPickerViewModel

interface IPickedFlexBoxRecycler {

    fun <M : Parcelable> setupRecyclerView(
        lifecycleOwner: LifecycleOwner,
        ctx: Context,
        view: View,
        @IdRes recyclerViewId: Int,
        adapter: RecyclerView.Adapter<*>,
        pickerViewModel: BaseItemPickerViewModel<M>,
        onChangeCallback: ((List<M>) -> Unit)? = null
    ) {
        val recyclerView: RecyclerView = view.findViewById(recyclerViewId)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = FlexboxLayoutManager(ctx)
        pickerViewModel.observePicked(lifecycleOwner) {
            adapter.notifyDataSetChanged()
            onChangeCallback?.invoke(it)
        }
    }
}