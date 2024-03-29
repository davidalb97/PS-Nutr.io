package pt.isel.ps.g06.httpserver.common

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.MalformedRestaurantIdentifierResponseStatusException
import pt.isel.ps.g06.httpserver.model.restaurant.RestaurantIdentifier

const val ID_SEPARATOR = "+"
private const val NUMBER_OF_IDS = 3

@Component
class RestaurantIdentifierBuilder {
    fun extractIdentifiers(restaurantId: String): RestaurantIdentifier {
        val values = restaurantId.split(ID_SEPARATOR, limit = NUMBER_OF_IDS)

        if (values.size != NUMBER_OF_IDS) {
            throw MalformedRestaurantIdentifierResponseStatusException(
                    "Cannot extract all identifiers from given String!\n" +
                            "Required: $NUMBER_OF_IDS\n" +
                            "Found: ${values.size}"
            )
        }

        val submitterId = values[0]
                .toIntOrNull()
                ?: throw MalformedRestaurantIdentifierResponseStatusException("Given submitter is not a number!")

        val submissionId = if (values[1].isBlank()) null
        else values[1]
                .toIntOrNull()
                ?: throw MalformedRestaurantIdentifierResponseStatusException("Given Submission is not a number!")

        val apiId = if (values[2].isBlank()) null
        else values[2]

        return RestaurantIdentifier(
                submitterId = submitterId,
                submissionId = submissionId,
                apiId = apiId
        )
    }
}

