package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.InsulinProfileDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto

private val isolationLevel = TransactionIsolationLevel.SERIALIZABLE
private val insulinProfileDaoClass = InsulinProfileDao::class.java

@Repository
class InsulinProfileDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getAllFromUser(submitterId: Int): Sequence<DbUserInsulinProfileDto> {
        val userInsulinProfiles = lazy {
            jdbi.inTransaction<Collection<DbUserInsulinProfileDto>, Exception>(isolationLevel) { handle ->
                return@inTransaction handle
                        .attach(insulinProfileDaoClass)
                        .getAllFromUser(submitterId)

            }
        }

        return Sequence { userInsulinProfiles.value.iterator() }
    }

    fun getFromUser(submitterId: Int, profileName: String): DbUserInsulinProfileDto {
        return jdbi.inTransaction<DbUserInsulinProfileDto, Exception>(isolationLevel) { handle ->
            return@inTransaction handle
                    .attach(insulinProfileDaoClass)
                    .getFromUser(submitterId, profileName)

        }
    }

    fun insertProfile(): DbUserInsulinProfileDto {
        TODO()
    }

    fun deleteProfile(): Boolean {
        TODO()
    }
}