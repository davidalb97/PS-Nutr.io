package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R

interface IRequiredTextInput {

    fun validateTextViews(ctx: Context, vararg textViews: TextView): Boolean {
        return textViews.all { textView ->
            textView.text.isNotBlank().also { isNotBlank ->
                if (!isNotBlank) {
                    textView.error = ctx.getString(R.string.field_required)
                }
            }
        }
    }

    fun validatePositiveFloatTextViews(ctx: Context, vararg textViews: TextView): Boolean {
        return textViews.all { textView ->
            getPositiveFloatOrNull(textView).let { float ->
                return@let if (float == null) {
                    textView.error = ctx.getString(R.string.field_required)
                    false
                } else true
            }
        }
    }

    fun TextView.toPositiveFloatOrNull(): Float? = getPositiveFloatOrNull(this)

    private fun getPositiveFloatOrNull(textView: TextView): Float? {
        val float = textView.text?.toString()?.toFloatOrNull()
        if (float != null && float > 0.0F) {
            return float
        }
        return null
    }

    fun TextView.onFinishEdit(callback: (String?) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                callback(s?.toString())
            }
        })
    }
}