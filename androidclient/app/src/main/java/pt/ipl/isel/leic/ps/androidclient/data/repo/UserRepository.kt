package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.DbUserMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.User
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class UserRepository {

    val userMapper = DbUserMapper()

    fun getAllUsers() = roomDb.userDao().getAll()

    fun getUserById(userId: Int) =
        roomDb.userDao().get(userId)

    fun createUser(userProfile: User) =
        AsyncWorker<Unit, Unit> {
            roomDb.userDao().insert(userMapper.mapToEntity(userProfile))
        }

    fun updateUser(userProfile: User) =
        AsyncWorker<Unit, Unit> {
            roomDb.userDao().insert(userMapper.mapToEntity(userProfile))
        }

    fun deleteUser(userProfile: User) =
        AsyncWorker<Unit, Unit> {
            roomDb.userDao().insert(userMapper.mapToEntity(userProfile))
        }
}