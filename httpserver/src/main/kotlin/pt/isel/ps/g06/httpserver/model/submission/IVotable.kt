package pt.isel.ps.g06.httpserver.model.submission

import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.Votes

interface IVotable {
    val votable: Votable
}

class Votable(val votes: Votes, private val getVote: (Submitter) -> VoteState) {

    private val userVotes: MutableMap<Submitter, VoteState> = mutableMapOf()

    fun getUserVote(submitter: Submitter): VoteState {
        return userVotes.computeIfAbsent(submitter, getVote)
    }
}

