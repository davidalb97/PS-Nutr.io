package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.vote

import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState

data class VotesInput(
    val isVotable: Boolean,
    val userVote: VoteState,
    val positive: Int,
    val negative: Int
)