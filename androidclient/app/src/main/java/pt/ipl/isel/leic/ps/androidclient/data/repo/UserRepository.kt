package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbUserEntity
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class UserRepository {

    fun getAllUsers() = roomDb.userDao().getAll()

    fun getUserById(userId: Int) = roomDb.userDao().get(userId)

    fun getUserByName() {
        TODO()
    }

    fun createUser(userProfile: DbUserEntity) =
        AsyncWorker<Unit, Unit> {
            roomDb.userDao().insert(userProfile)
        }

    fun updateUser(userProfile: DbUserEntity) =
        AsyncWorker<Unit, Unit> {
            roomDb.userDao().insert(userProfile)
        }

    fun deleteUser(userProfile: DbUserEntity) =
        AsyncWorker<Unit, Unit> {
            roomDb.userDao().insert(userProfile)
        }
}