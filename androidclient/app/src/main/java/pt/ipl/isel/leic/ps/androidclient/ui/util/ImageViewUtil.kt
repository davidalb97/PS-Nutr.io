package pt.ipl.isel.leic.ps.androidclient.ui.util

import android.content.Context
import android.graphics.PorterDuff
import android.widget.ImageView
import androidx.annotation.ColorRes

fun ImageView.changeColor(ctx: Context, @ColorRes colorId: Int) {
    setColorFilter(
        ctx.resources.getColor(colorId),
        PorterDuff.Mode.SRC_ATOP
    )
}