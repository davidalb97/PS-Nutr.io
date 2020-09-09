package pt.ipl.isel.leic.ps.androidclient.data.model

enum class VoteState {
    POSITIVE,
    NEGATIVE,
    NOT_VOTED;

    fun nextState(isPositive: Boolean): VoteState {
        return when (this) {
            NOT_VOTED -> if (isPositive) POSITIVE else NEGATIVE
            POSITIVE -> if (isPositive) NOT_VOTED else NEGATIVE
            NEGATIVE -> if (isPositive) POSITIVE else NOT_VOTED
        }
    }
}