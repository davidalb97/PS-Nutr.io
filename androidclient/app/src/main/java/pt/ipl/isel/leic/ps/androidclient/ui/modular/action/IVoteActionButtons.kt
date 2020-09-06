package pt.ipl.isel.leic.ps.androidclient.ui.modular.action

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IVoteProgress
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction

interface IVoteActionButtons : IVoteProgress, IContext, IAction, IUserSession, ILog {

    val upVoteButtonId: Int
    var upVoteButton: ImageButton
    val downVoteButtonId: Int
    var downVoteButton: ImageButton
    val voteButtonsLayoutId: Int
    var voteButtonsLayout: ViewGroup

    fun onVote(voteState: VoteState, onSuccess: () -> Unit, onError: (Throwable) -> Unit)

    fun setupVoteButtons(view: View, votes: Votes?) {
        upVoteButton = view.findViewById(upVoteButtonId)
        downVoteButton = view.findViewById(downVoteButtonId)
        voteButtonsLayout = view.findViewById(voteButtonsLayoutId)

        val userVote = votes?.userHasVoted
        val ctx = fetchCtx()

        if (!actions.contains(ItemAction.VOTE) || votes == null || !votes.isVotable) {
            return
        }

        if (userVote == VoteState.POSITIVE) {
            upVoteButton.background =
                ContextCompat.getDrawable(ctx, R.drawable.upvote_button_triggered)
        }
        if (userVote == VoteState.NEGATIVE) {
            downVoteButton.background =
                ContextCompat.getDrawable(ctx, R.drawable.downvote_button_triggered)
        }

        upVoteButton.setOnClickListener {
            ensureUserSession(ctx) {
                vote(votes, true)
            }
        }
        downVoteButton.setOnClickListener {
            ensureUserSession(ctx) {
                vote(votes, false)
            }
        }
        voteButtonsLayout.visibility = View.VISIBLE
    }

    private fun vote(votes: Votes, isPositive: Boolean) {
        val currentState = votes.userHasVoted
        val nextVoteState = currentState.nextState(isPositive)
        onVote(
            voteState = nextVoteState,
            onSuccess = {
                changeVoteButtonColors(currentState, nextVoteState)
            },
            onError = log::e
        )
    }

    fun changeVoteButtonColors(previousState: VoteState, currentState: VoteState) {
        val ctx = fetchCtx()
        if (previousState == VoteState.POSITIVE) {
            upVoteButton.background =
                ContextCompat.getDrawable(ctx, R.drawable.upvote_button)
        } else if (previousState == VoteState.NEGATIVE) {
            downVoteButton.background =
                ContextCompat.getDrawable(ctx, R.drawable.downvote_button)
        }
        if (currentState == VoteState.POSITIVE) {
            upVoteButton.background =
                ContextCompat.getDrawable(ctx, R.drawable.upvote_button_triggered)
        } else if (currentState == VoteState.NEGATIVE) {
            downVoteButton.background =
                ContextCompat.getDrawable(ctx, R.drawable.downvote_button_triggered)
        }
    }
}