package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.vote.VotesInput
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class InputVotesMapper {

    fun mapToModel(dto: VotesInput?): Votes? = dto?.let {
        Votes(
            isVotable = dto.isVotable,
            userHasVoted = dto.userVote,
            positive = dto.positive,
            negative = dto.negative
        )
    }
}