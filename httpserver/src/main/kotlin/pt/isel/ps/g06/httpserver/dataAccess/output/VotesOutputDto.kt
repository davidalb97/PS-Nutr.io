package pt.isel.ps.g06.httpserver.dataAccess.output

data class VotesOutputDto(
        val userVote: Boolean,
        val positive: Int,
        val negative: Int
)