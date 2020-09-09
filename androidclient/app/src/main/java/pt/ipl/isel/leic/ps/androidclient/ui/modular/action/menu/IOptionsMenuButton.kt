package pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu

import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment

interface IOptionsMenuButton {

    var Fragment.menuHolder: Menu

    @CallSuper
    fun Fragment.onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menuHolder = menu
    }
}