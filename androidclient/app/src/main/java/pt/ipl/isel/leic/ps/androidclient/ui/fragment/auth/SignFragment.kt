package pt.ipl.isel.leic.ps.androidclient.ui.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import pt.ipl.isel.leic.ps.androidclient.R

class SignFragment : Fragment() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.loginButton -> {
                    setFragment(LoginFragment())
                    true
                }
                R.id.registerButton -> {
                    setFragment(RegisterFragment())
                    true
                }
                else -> false
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationBar=
            view.findViewById<BottomNavigationView>(R.id.bottomNavigationBar)

        setFragment(LoginFragment())

        bottomNavigationBar.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
    }

    private fun setFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}