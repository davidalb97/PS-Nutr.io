package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

interface IImage {

    val image: ImageView

    fun setupImage(view: View, imageUri: Uri?) {
        if (imageUri != null) {
            Glide.with(view)
                .load(imageUri)
                .into(image)
            image.visibility = View.VISIBLE
        }
    }
}