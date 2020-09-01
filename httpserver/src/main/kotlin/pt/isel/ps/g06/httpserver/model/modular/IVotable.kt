package pt.isel.ps.g06.httpserver.model.modular

import pt.isel.ps.g06.httpserver.model.Votes

interface IVotable {
    val isVotable: UserPredicate
    val userVote: UserVote
    val votes: Lazy<Votes>
}