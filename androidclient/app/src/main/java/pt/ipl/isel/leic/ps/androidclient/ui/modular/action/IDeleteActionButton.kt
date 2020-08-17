package pt.ipl.isel.leic.ps.androidclient.ui.modular.action

import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView.Adapter
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.modular.viewHolder.IPressAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction

interface IDeleteActionButton<T : Any> : IPressAction<T>, ILog {

    val deleteButton: ImageButton

    fun onDelete(onSuccess: () -> Unit, onError: (Throwable) -> Unit)

    fun setupOnDeleteAction(adapter: Adapter<*>?, position: Int) {
        if (!actions.contains(ItemAction.DELETE)) {
            return
        }
        deleteButton.setOnClickListener {
            onDelete(
                onSuccess = {
                    adapter?.notifyItemRemoved(position)
                },
                onError = log::e
            )
            setButtonsVisibility(false)
        }
        deleteButton.visibility = View.VISIBLE
    }
}