package pt.ipl.isel.leic.ps.androidclient.data.api.mapper

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.VotesInputDto
import pt.ipl.isel.leic.ps.androidclient.data.model.Votes

class InputVotesMapper {

    fun mapToModel(dto: VotesInputDto?): Votes? = dto?.let {
        Votes(
            userHasVoted = dto.userHasVoted,
            positive = dto.positive,
            negative = dto.negative
        )
    }
}