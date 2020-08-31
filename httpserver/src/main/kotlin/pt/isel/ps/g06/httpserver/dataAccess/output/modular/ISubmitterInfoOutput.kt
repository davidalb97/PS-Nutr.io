package pt.isel.ps.g06.httpserver.dataAccess.output.modular

import pt.isel.ps.g06.httpserver.dataAccess.output.user.SimplifiedUserOutput

interface ISubmitterInfoOutput {
    val createdBy: SimplifiedUserOutput?
}