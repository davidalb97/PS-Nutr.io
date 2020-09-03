package pt.ipl.isel.leic.ps.androidclient.ui.modular.action

import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import pt.ipl.isel.leic.ps.androidclient.R
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IContext
import pt.ipl.isel.leic.ps.androidclient.ui.modular.ILog
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IUserSession
import pt.ipl.isel.leic.ps.androidclient.ui.modular.IVoteProgress
import pt.ipl.isel.leic.ps.androidclient.ui.util.ItemAction
import pt.ipl.isel.leic.ps.androidclient.ui.util.changeColor

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

        if (!actions.contains(ItemAction.VOTE) || votes == null || !votes.isVotable) {
            return
        }
        upVoteButton.setOnClickListener {
            ensureUserSession(fetchCtx()) {
                vote(votes, true)
            }
        }
        downVoteButton.setOnClickListener {
            ensureUserSession(fetchCtx()) {
                vote(votes, false)
            }
        }
        voteButtonsLayout.visibility = View.VISIBLE
    }

    private fun vote(votes: Votes, upVote: Boolean) {
        val currentState = votes.userHasVoted
        val nextVoteState = currentState.nextState(upVote)
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
            upVoteButton.changeColor(ctx, R.color.colorGray)
        } else if (previousState == VoteState.NEGATIVE) {
            downVoteButton.changeColor(ctx, R.color.colorGray)
        }
        if (currentState == VoteState.POSITIVE) {
            upVoteButton.changeColor(ctx, R.color.colorAccent)
        } else if (currentState == VoteState.NEGATIVE) {
            downVoteButton.changeColor(ctx, R.color.colorAccent)
        }
    }
}