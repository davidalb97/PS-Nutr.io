package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.VoteOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState

class OutputVoteMapper {

    fun mapToOutputModel(model: VoteState) = VoteOutput(
        vote = model
    )
}