package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi

class UserDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi)