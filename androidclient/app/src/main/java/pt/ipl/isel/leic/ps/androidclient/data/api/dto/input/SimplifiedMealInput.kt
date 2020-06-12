package pt.ipl.isel.leic.ps.androidclient.data.api.dto.input

import java.net.URI

open class SimplifiedMealInput(
    var id: Int,
    var name: String,
    var image: URI?,
    var isFavorite: Boolean
)