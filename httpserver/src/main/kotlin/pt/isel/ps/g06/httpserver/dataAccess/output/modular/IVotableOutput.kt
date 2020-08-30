package pt.isel.ps.g06.httpserver.dataAccess.output.modular

import pt.isel.ps.g06.httpserver.dataAccess.output.vote.VotesOutput

interface IVotableOutput {
    val votes: VotesOutput
}