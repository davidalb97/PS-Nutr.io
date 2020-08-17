package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.view.View
import androidx.cardview.widget.CardView

interface ILoading {

    val loadingCard: CardView

    fun setupLoading() {
        loadingCard.visibility = View.GONE
    }

    fun startLoading() {
        loadingCard.visibility = View.VISIBLE
    }

    fun stopLoading() {
        loadingCard.visibility = View.GONE
    }
}