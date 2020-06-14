package pt.isel.ps.g06.httpserver.common

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.common.exception.clientError.MalformedRestaurantIdentifierException
import pt.isel.ps.g06.httpserver.model.RestaurantIdentifier

const val ID_SEPARATOR = "+"
private const val NUMBER_OF_IDS = 3

@Component
class RestaurantIdentifierBuilder {
    fun extractIdentifiers(restaurantId: String): RestaurantIdentifier {
        val values = restaurantId.split(ID_SEPARATOR, limit = NUMBER_OF_IDS)

        if (values.size != NUMBER_OF_IDS) {
            throw MalformedRestaurantIdentifierException(
                    """
                    Cannot extract all identifiers from given String!
                    Required: $NUMBER_OF_IDS
                    Found: ${values.size}
                    """
            )
        }

        val submitterId = values[0]
                .toIntOrNull()
                ?: throw MalformedRestaurantIdentifierException("Given submitter is not a number!")

        val submissionId = if (values[1].isBlank()) null
        else values[1]
                .toIntOrNull()
                ?: throw MalformedRestaurantIdentifierException("Given Submission is not a number!")

        val apiId = if (values[2].isBlank()) null
        else values[2]

        return RestaurantIdentifier(
                submitterId = submitterId,
                submissionId = submissionId,
                apiId = apiId
        )
    }
}

