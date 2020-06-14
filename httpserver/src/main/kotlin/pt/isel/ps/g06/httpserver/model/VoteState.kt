package pt.isel.ps.g06.httpserver.model

//TODO Pass these to a more readable string on Jackson
enum class VoteState {
    POSITIVE,
    NEGATIVE,
    NOT_VOTED;
}