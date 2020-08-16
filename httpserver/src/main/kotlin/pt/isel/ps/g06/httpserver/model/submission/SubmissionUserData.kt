package pt.isel.ps.g06.httpserver.model.submission

import pt.isel.ps.g06.httpserver.model.VoteState

/**
 * Represents all metadata for a given User that a submission *might* have.
 * A null field means that the submission does not support given attribute, e.g: A null [VoteState] means
 * the submission is not Votable.
 */
data class SubmissionUserData(
        val voteState: VoteState?,
        val isFavorite: Boolean?
)