package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VoteDao

class DbVotableRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val voteClass = VoteDao::class.java

    fun insertVote(
            submitterId: Int,
            submissionId: Int,
            vote: Boolean
    ): Boolean {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Boolean, Exception>(serializable) {
                val voteDao = it.attach(voteClass)
                voteDao.insert(submitterId, submissionId, vote)
            }
        }
    }

    fun deleteVote(
            submitterId: Int,
            submissionId: Int
    ): Boolean {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Boolean, Exception>(serializable) {
                val voteDao = it.attach(voteClass)
                voteDao.delete(submitterId, submissionId)
            }
        }
    }

    fun updateVote(
            submitterId: Int,
            submissionId: Int,
            vote: Boolean
    ): Boolean {
        return jdbi.open().use { handle ->
            return handle.inTransaction<Boolean, Exception>(serializable) {
                val voteDao = it.attach(voteClass)
                voteDao.update(submitterId, submissionId, vote)
            }
        }
    }
}