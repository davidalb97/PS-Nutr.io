package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

data class VotesInputDto(
    val userHasVoted: Boolean?,
    val positive: Int,
    val negative: Int
)