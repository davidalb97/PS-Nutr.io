package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import org.springframework.stereotype.Repository
import pt.isel.ps.g06.httpserver.common.exception.clientError.SubmissionNotFavoritableException
import pt.isel.ps.g06.httpserver.common.exception.notFound.SubmissionNotFoundException
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.FavoriteDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionContractDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao

@Repository
class FavoriteDbRepository(jdbi: Jdbi) : BaseDbRepo(jdbi) {

    fun getFavorite(
            submissionId: Int,
            userId: Int,
            isolationLevel: TransactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE
    ): Boolean {
        return jdbi.inTransaction<Boolean, Exception>(isolationLevel) { handle ->
            return@inTransaction handle.attach(FavoriteDao::class.java)
                    .getByIds(submissionId, userId)
                    ?.let { true }
                    ?: false
        }
    }

    fun setFavorite(
            submissionId: Int,
            userId: Int,
            isFavorite: Boolean,
            isolationLevel: TransactionIsolationLevel = TransactionIsolationLevel.SERIALIZABLE
    ): Boolean {
        return jdbi.inTransaction<Boolean, Exception>(isolationLevel) { handle ->
            return@inTransaction userId.let {

                handle.attach(SubmissionDao::class.java)
                        .getById(submissionId)
                        ?: throw SubmissionNotFoundException()

                val contracts = handle.attach(SubmissionContractDao::class.java)
                        .getAllById(submissionId)

                if(contracts.none { it.submission_contract == SubmissionContractType.FAVORABLE.toString() }) {
                    throw SubmissionNotFavoritableException()
                }
                val favoriteDao = handle.attach(FavoriteDao::class.java)
                val favoriteExists = getFavorite(submissionId, userId, isolationLevel)

                if(favoriteExists && !isFavorite) {
                    favoriteDao.delete(submissionId, userId)
                }
                if(!favoriteExists && isFavorite) {
                    favoriteDao.insert(submissionId, userId)
                }

                true
            }
        }
    }
}