package pt.ipl.isel.leic.ps.androidclient.ui.modular

import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

interface IVoteProgress {

    val voteCountersLayoutId: Int
    var voteCountersLayout: ViewGroup
    val votesBarId: Int
    var votesBar: ProgressBar
    val upVoteCounterId: Int
    var upVoteCounter: TextView
    val downVoteCounterId: Int
    var downVoteCounter: TextView

    fun setupVoteBarCounters(view: View, votes: Votes?) {
        voteCountersLayout = view.findViewById(voteCountersLayoutId)
        votesBar = view.findViewById(votesBarId)
        upVoteCounter = view.findViewById(upVoteCounterId)
        downVoteCounter = view.findViewById(downVoteCounterId)

        if(votes == null || !votes.hasVotes()) {
            //Set visibility to GONE to avoid recycled view holders invalid info
            voteCountersLayout.visibility = View.GONE
            return
        }

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