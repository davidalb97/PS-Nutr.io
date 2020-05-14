package pt.isel.ps.g06.httpserver.dataAccess.input

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class RestaurantInput(
        @field:NotBlank(message = "Restaurant name must not be empty!")
        val name: String?,
        @field:NotNull(message = "A latitude must be given!")
        val latitude: Float?,
        @field:NotNull(message = "A longitude must be given!")
        val longitude: Float?,
        val cuisines: Collection<String>?,
        val submitterId: Int?
)