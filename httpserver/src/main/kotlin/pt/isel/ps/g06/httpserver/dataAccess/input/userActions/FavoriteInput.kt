package pt.isel.ps.g06.httpserver.dataAccess.input.userActions

import javax.validation.constraints.NotNull

data class FavoriteInput(
        @field:NotNull(message = "A favorite must be a boolean value!")
        val isFavorite: Boolean?
)