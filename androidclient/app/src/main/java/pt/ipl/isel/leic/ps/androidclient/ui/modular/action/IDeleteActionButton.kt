package pt.ipl.isel.leic.ps.androidclient.ui.modular.action

import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView.Adapter
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.viewHolder.IPressAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction

interface IDeleteActionButton<T : Any> : IPressAction<T>, IUserSession, IContext, ILog {

    val deleteButtonId: Int
    var deleteButton: ImageButton

    fun onDelete(onSuccess: () -> Unit, onError: (Throwable) -> Unit)

    fun setupOnDeleteAction(view: View, adapter: Adapter<*>?, position: Int) {
        deleteButton = view.findViewById(deleteButtonId)

        if (!actions.contains(ItemAction.DELETE)) {
            return
        }
        deleteButton.setOnClickListener {
            onDelete(
                onSuccess = {
                    Toast.makeText(fetchCtx(), R.string.item_deleted, Toast.LENGTH_SHORT)
                        .show()
                },
                onError = {
                    Toast.makeText(fetchCtx(), R.string.item_delete_fail, Toast.LENGTH_SHORT)
                        .show()
                    log.e(it)
                }
            )
            setButtonsVisibility(false)
        }
        deleteButton.visibility = View.VISIBLE
    }
}