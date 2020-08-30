package pt.isel.ps.g06.httpserver.model.modular

import pt.isel.ps.g06.httpserver.model.Portion
import pt.isel.ps.g06.httpserver.model.VoteState

typealias UserPortion = (Int) -> Portion?

typealias UserVote = (Int?) -> VoteState

typealias UserPredicate = (Int?) -> Boolean

fun toUserPredicate(default: () -> Boolean, onUser: (Int) -> Boolean): UserPredicate {
    return { userId -> userId?.let { onUser(it) } ?: default() }
}

fun toUserVote(onUser: (Int) -> VoteState): UserVote {
    return { userId -> userId?.let { onUser(it) } ?: VoteState.NOT_VOTED }
}