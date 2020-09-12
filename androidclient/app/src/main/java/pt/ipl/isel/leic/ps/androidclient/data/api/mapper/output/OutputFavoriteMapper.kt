package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.FavoriteOutput

class OutputFavoriteMapper {

    fun mapToOutputModel(isFavorite: Boolean) = FavoriteOutput(
        isFavorite = isFavorite
    )
}