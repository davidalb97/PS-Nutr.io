package pt.isel.ps.g06.httpserver.dataAccess.db.dto.meal

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.submitter.DbSubmissionSubmitterInfoDto

/**
 * Container for a Meal that may also have user information
 */
data class DbSubmitterMealDto(
        val dbMealDto: DbMealDto,
        val dbSubmissionSubmitterInfoDto: DbSubmissionSubmitterInfoDto?
)