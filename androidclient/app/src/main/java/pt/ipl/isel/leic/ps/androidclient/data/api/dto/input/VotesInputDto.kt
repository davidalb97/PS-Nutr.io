package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState

data class VotesInputDto(
    val userHasVoted: VoteState,
    val positive: Int,
    val negative: Int
)