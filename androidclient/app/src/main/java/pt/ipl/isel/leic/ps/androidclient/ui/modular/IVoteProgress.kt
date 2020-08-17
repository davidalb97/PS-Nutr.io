package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

interface IVoteProgress {

    val voteCountersLayout: ViewGroup
    val votesBar: ProgressBar
    val upVoteCounter: TextView
    val downVoteCounter: TextView

    fun setupVoteBarCounters(votes: Votes?) {
        votes ?: return

        val upVotes = votes.positive
        val downVotes = votes.negative
        val totalVotes = upVotes + downVotes
        votesBar.progress = 0
        if (totalVotes > 0) {
            votesBar.progress = upVotes / totalVotes * 100
        }
        upVoteCounter.text = upVotes.toString()
        downVoteCounter.text = downVotes.toString()

        voteCountersLayout.visibility = View.VISIBLE
    }
}