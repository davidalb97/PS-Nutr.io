package pt.isel.ps.g06.httpserver.dataAccess.db.repo

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.VoteDao
import pt.isel.ps.g06.httpserver.dataAccess.model.Votes

class DbVotableRepository(private val jdbi: Jdbi) {

    val serializable = TransactionIsolationLevel.SERIALIZABLE
    val voteClass = VoteDao::class.java

    fun getVotes(submitterId: Int, submissionId: Int): Votes? {
        return inTransaction<Votes>(jdbi, serializable) {
            throw UnsupportedOperationException()
        }
    }

    fun insertVote(
            submitterId: Int,
            submissionId: Int,
            vote: Boolean
    ): Boolean {
        return inTransaction(jdbi, serializable) {
            val voteDao = it.attach(voteClass)
            voteDao.insert(submitterId, submissionId, vote)
            true
        }
    }

    fun deleteVote(
            submitterId: Int,
            submissionId: Int
    ): Boolean {
        return inTransaction(jdbi, serializable) {
            val voteDao = it.attach(voteClass)
            voteDao.delete(submitterId, submissionId)
            true
        }
    }

    fun updateVote(
            submitterId: Int,
            submissionId: Int,
            vote: Boolean
    ): Boolean {
        return inTransaction(jdbi, serializable) {
            val voteDao = it.attach(voteClass)
            voteDao.update(submitterId, submissionId, vote)
            true
        }
    }
}