package pt.ipl.isel.leic.ps.androidclient.data.model

enum class VoteState {
    POSITIVE,
    NEGATIVE,
    NOT_VOTED;

    fun nextState(upVote: Boolean): VoteState {
        return when (this) {
            NOT_VOTED -> if (upVote) POSITIVE else NEGATIVE
            POSITIVE -> if (upVote) NOT_VOTED else POSITIVE
            NEGATIVE -> if (upVote) NEGATIVE else NOT_VOTED
        }
    }
}