package pt.ipl.isel.leic.ps.androidclient.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import pt.ipl.isel.leic.ps.androidclient.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonActions = mapOf<RelativeLayout, Int>(
            Pair(view.findViewById(R.id.firstSection_button1), R.id.nav_tab_restaurants),
            Pair(view.findViewById(R.id.firstSection_button2), R.id.nav_tab_meals),
            Pair(view.findViewById(R.id.secondSection_button1), R.id.nav_tab_restaurants),
            Pair(view.findViewById(R.id.secondSection_button2), R.id.nav_tab_restaurants)
        )

        buttonActions.forEach { button ->
            button.key.setOnClickListener {
                view.findNavController().navigate(button.value)
            }
        }
    }
}
