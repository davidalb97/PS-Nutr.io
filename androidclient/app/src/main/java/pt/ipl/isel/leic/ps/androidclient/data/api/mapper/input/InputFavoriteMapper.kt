package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.input.favorite.FavoritesInput
import pt.ipl.isel.leic.ps.androidclient.data.model.Favorites

class InputFavoriteMapper {

    fun mapToModel(dto: FavoritesInput) : Favorites =
        Favorites(
            isFavorable = dto.isFavorable,
            isFavorite = dto.isFavorite
        )
}