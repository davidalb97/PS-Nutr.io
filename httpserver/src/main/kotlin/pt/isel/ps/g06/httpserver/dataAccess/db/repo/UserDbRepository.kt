package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import pt.isel.ps.g06.httpserver.springConfig.DbConfig

class UserDbRepository(jdbi: Jdbi, config: DbConfig) : BaseDbRepo(jdbi, config)