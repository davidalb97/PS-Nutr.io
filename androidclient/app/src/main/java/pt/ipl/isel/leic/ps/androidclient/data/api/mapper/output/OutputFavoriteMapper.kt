package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.FavoriteOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.VoteOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.VoteState

class OutputFavoriteMapper {

    fun mapToOutputModel(isFavorite: Boolean) = FavoriteOutput(
        isFavorite = isFavorite
    )
}