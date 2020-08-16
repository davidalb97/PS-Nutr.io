package pt.isel.ps.g06.httpserver.dataAccess.db.dto.submitter

import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbFavoriteDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbSubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserVoteDto

/**
 * Represents information about a given user
 * for a submission, such as votes and favorites.
 */
data class DbSubmissionSubmitterInfoDto(
        val submitterDto: DbSubmitterDto,
        val dbFavoriteDto: DbFavoriteDto?,
        val dbUserVoteDto: DbUserVoteDto?
)