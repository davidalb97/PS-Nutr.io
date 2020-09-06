package pt.ipl.isel.leic.ps.androidclient.ui.modular.action.menu

import android.view.Menu
import android.view.MenuItem
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.action.IAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.prompt.PromptInput

interface IReportMenuItem : IMenu, IContext, IAction, ILog, IUserSession {

    fun onReport(reportStr: String, onSuccess: () -> Unit, onError: (Throwable) -> Unit)

    fun setupReportMenuItem(isReportable: Boolean) {
        if (!actions.contains(ItemAction.REPORT) || !isReportable) {
            return
        }
        menus["report"] = object : MenuItemFactory() {
            override fun newMenuItem(menu: Menu): MenuItem {
                return menu.add(R.string.report_menu_item_title).also { menuItem ->
                    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_WITH_TEXT)
                    menuItem.setOnMenuItemClickListener {
                        ensureUserSession(fetchCtx()) {
                            PromptInput(
                                ctx = fetchCtx(),
                                titleId = R.string.report_menu_item_title,
                                confirmConsumer = { reportMsg ->
                                    onReport(
                                        reportStr = reportMsg,
                                        onSuccess = menu::close,
                                        onError = log::e
                                    )
                                }
                            ).show()
                        }
                        true
                    }
                }
            }
        }
    }
}