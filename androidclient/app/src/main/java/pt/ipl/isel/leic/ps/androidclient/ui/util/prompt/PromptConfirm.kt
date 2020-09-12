package pt.ipl.isel.leic.ps.androidclient.ui.util.prompt

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import pt.ipl.isel.leic.ps.androidclient.R

class PromptConfirm(
    ctx: Context,
    @StringRes titleId: Int,
    @StringRes messageId: Int? = null,
    clickListener: () -> Unit
) {

    private var alertDialog = AlertDialog.Builder(ctx)
        .setTitle(titleId)
        .setPositiveButton(R.string.confirm) { _, _ -> clickListener() }
        .setNegativeButton(R.string.no) { _, _ -> }
        .setCancelable(true)
        .also {
            if (messageId != null) {
                it.setMessage(messageId)
            }
        }
        .create()

    fun show(message: String) {
        alertDialog.setMessage(message)
        alertDialog.show()
    }

    fun show() {
        alertDialog.show()
    }
}