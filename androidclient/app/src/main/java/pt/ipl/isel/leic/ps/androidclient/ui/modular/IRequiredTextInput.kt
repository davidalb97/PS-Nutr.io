package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.content.Context
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R

interface IRequiredTextInput {

    fun validateTextViews(ctx: Context, vararg textViews: TextView): Boolean {
        return textViews.all {  textView ->
            textView.text.isNotBlank().also { isNotBlank ->
                if(!isNotBlank) {
                    textView.error = ctx.getString(R.string.field_required)
                }
            }
        }
    }
}