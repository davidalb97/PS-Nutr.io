package pt.ipl.isel.leic.ps.androidclient.ui.cuisines

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import pt.ipl.isel.leic.ps.androidclient.R

class CuisinesFragment : Fragment() {

    companion object {
        fun newInstance() =
            CuisinesFragment()
    }

    private lateinit var viewModel: CuisinesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cuisines, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CuisinesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
