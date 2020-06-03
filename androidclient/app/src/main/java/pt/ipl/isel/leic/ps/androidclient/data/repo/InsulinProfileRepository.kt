package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class InsulinProfileRepository {


    fun getAllProfiles(): LiveData<List<DbInsulinProfile>> =
        roomDb.insulinProfileDao().getAll()


    fun getProfile(name: String): DbInsulinProfile =
        roomDb.insulinProfileDao().get(name)


    fun addProfile(profileDb: pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfile) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().insert(profileDb)
        }.execute()

    fun updateProfile(profileDb: pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfile) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().update(profileDb)
        }

    fun deleteProfile(profileDb: pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfile) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().delete(profileDb)
        }.execute()
}

