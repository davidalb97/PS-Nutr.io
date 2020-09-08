package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.ui.fragment.BaseFragment

class AboutFragment : BaseFragment() {

    override val layout: Int = R.layout.about_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linkIntents = hashMapOf<RelativeLayout, String>(
            Pair(
                view.findViewById(R.id.github_link_pedro),
                view.findViewById<TextView>(R.id.link_pedro).text.toString()
            ),
            Pair(
                view.findViewById(
                    R.id.github_link_david
                ),
                view.findViewById<TextView>(R.id.link_david).text.toString()
            ),
            Pair(
                view.findViewById(R.id.github_link_miguel),
                view.findViewById<TextView>(R.id.link_miguel).text.toString()
            )
        )

        linkIntents.forEach { link ->
            link.key.setOnClickListener { navigateToLink(link.value) }
        }
    }

    private fun navigateToLink(uri: String) {
        val parsedUri = Uri.parse(uri)
        startActivity(Intent(Intent.ACTION_VIEW, parsedUri))
    }
}
