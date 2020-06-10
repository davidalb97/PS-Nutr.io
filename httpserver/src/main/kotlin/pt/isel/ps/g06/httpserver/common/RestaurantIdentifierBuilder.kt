package pt.isel.ps.g06.httpserver.common

import org.springframework.stereotype.Component

private const val ID_SEPARATOR = ";"
private const val NUMBER_OF_IDS = 3

@Component
class RestaurantIdentifierBuilder {
    fun buildRestaurantId(identifiers: RestaurantIdentifiers): String {
        return StringBuilder(identifiers.submitterId)
                .append(identifiers.submitterId)
                .append(identifiers.apiId)
                .toString()
    }

    //TODO Better exception: MalformedInput/MalformedIdentifier whenever exception is thrown
    fun extractIdentifiers(restaurantId: String): RestaurantIdentifiers {
        val values = restaurantId.split(ID_SEPARATOR, limit = NUMBER_OF_IDS)

        if (values.size != NUMBER_OF_IDS) {
            throw IllegalStateException("Cannot extract identifiers from given string!")
        }

        val submitterId = values[0].toIntOrNull() ?: throw IllegalStateException()

        val submissionId = if (values[1].isBlank()) null
        else values[2].toIntOrNull() ?: throw IllegalStateException()

        val apiId = if (values[2].isBlank()) null else values[2]

        return RestaurantIdentifiers(
                submitterId = submitterId,
                submissionId = submissionId,
                apiId = apiId
        )
    }
}

data class RestaurantIdentifiers(val submitterId: Int, val submissionId: Int?, val apiId: String?)