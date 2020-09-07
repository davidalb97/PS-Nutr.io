package pt.ipl.isel.leic.ps.androidclient.ui.util.prompt

import android.content.Context
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import pt.ipl.isel.leic.ps.androidclient.R

class PromptInput(
    ctx: Context,
    @StringRes titleId: Int,
    inputType: Int = InputType.TYPE_CLASS_TEXT,
    confirmConsumer: (String) -> Unit,
    cancelConsumer: (String) -> Unit = { }
) {

    private val editText = EditText(ctx).apply {
        this.inputType = inputType
    }
    private val imm = ctx.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    private var alertDialog = AlertDialog.Builder(ctx)
        .setTitle(titleId)
        .setView(editText)
        .setPositiveButton(R.string.ok) { _, _ ->
            confirmConsumer(editText.text.toString())
        }
        .setNegativeButton(R.string.Dialog_cancel) { _, _ ->
            cancelConsumer(editText.text.toString())
        }
        .setOnDismissListener {
            hideInput()
        }
        .create()

    fun show() {
        clear()                 //Clears old text
        alertDialog.show()
        editText.requestFocus()
        showInput()
    }

    fun clear() {
        editText.text.clear()
    }

    private fun hideInput() {
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    private fun showInput() {
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }
}