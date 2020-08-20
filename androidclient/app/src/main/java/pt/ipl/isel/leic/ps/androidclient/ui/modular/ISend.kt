package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.ui.util.Navigation

interface ISend {

    fun onSendToDestination(bundle: Bundle)

    fun sendToDestination(view: View, destination: Navigation) {
        if (destination != Navigation.IGNORE) {
            val bundle = Bundle()
            onSendToDestination(bundle)
            view.findNavController().navigate(destination.navId, bundle)
        }
    }
}