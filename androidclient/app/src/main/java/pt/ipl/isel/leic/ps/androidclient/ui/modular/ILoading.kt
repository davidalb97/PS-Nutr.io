package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.view.View
import androidx.cardview.widget.CardView

interface ILoading {

    val loadingCardId: Int
    var loadingCard: CardView

    fun setupLoading(view: View) {
        loadingCard = view.findViewById(loadingCardId)
        loadingCard.visibility = View.GONE
    }

    fun startLoading() {
        loadingCard.visibility = View.VISIBLE
    }

    fun stopLoading() {
        loadingCard.visibility = View.GONE
    }
}