package pt.ipl.isel.leic.ps.androidclient.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.navGraphViewModels
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IViewModelManager
import pt.ipl.isel.leic.ps.androidclient.ui.util.getParentNavigation

interface IParentViewModel : IViewModelManager

inline fun <reified VM : ViewModel, F> F.navParentViewModel(): Lazy<VM> where F : Fragment, F : IParentViewModel {
    return lazy {
        val parentNavigation = requireNotNull(arguments?.getParentNavigation()) {
            "${javaClass.simpleName} Must have a parent navigation for navGraphViewModel!"
        }
        val viewModelLazy: VM by navGraphViewModels(parentNavigation.navId) {
            vMProviderFactorySupplier(arguments, savedInstanceState, requireActivity().intent)
        }
        viewModelLazy
    }
}