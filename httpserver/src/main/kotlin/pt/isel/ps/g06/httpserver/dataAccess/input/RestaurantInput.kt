package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class RestaurantInput(
        @NotBlank(message = "Restaurant name must not be empty!")
        val name: String?,
        @NotNull(message = "A latitude must be given!")
        val latitude: Float?,
        @NotNull(message = "A longitude must be given!")
        val longitude: Float?,
        val cuisines: Collection<String>?,
        val submitterId: Int?
)