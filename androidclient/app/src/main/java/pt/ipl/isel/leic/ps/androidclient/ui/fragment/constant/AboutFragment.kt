package pt.ipl.isel.leic.ps.androidclient.ui.fragment.constant

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import pt.ipl.isel.leic.ps.androidclient.R

class AboutFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val linkIntents = hashMapOf<RelativeLayout, String>(
            Pair(
                view.findViewById(R.id.github_link_pedro),
                view.findViewById<TextView>(R.id.link_pedro).text.toString()
            ),
            Pair(view.findViewById(
                R.id.github_link_david),
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
