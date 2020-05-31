package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.DbInsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class InsulinProfileRepository {

    fun getAllProfiles() =
        roomDb.insulinProfileDao().getAll()

    fun getProfile(name: String): DbInsulinProfileDto =
        roomDb.insulinProfileDao().get(name)

    fun addProfile(profileDb: DbInsulinProfileDto) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().insert(profileDb)
        }.execute()

    fun updateProfile(profileDb: DbInsulinProfileDto) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().update(profileDb)
        }

    fun deleteProfile(profileDb: DbInsulinProfileDto) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().delete(profileDb)
        }.execute()
}

