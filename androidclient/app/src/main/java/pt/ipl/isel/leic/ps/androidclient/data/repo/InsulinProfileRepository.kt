package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.source.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class InsulinProfileRepository {

    fun getAllProfiles(): LiveData<List<InsulinProfile>> {
        return roomDb.insulinProfileDao().getAll()
    }

    fun getProfile(name: String): InsulinProfile {
        return roomDb.insulinProfileDao().get(name)
    }

    fun addProfile(profile: InsulinProfile) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().insert(profile)
        }.execute()

    fun updateProfile(profile: InsulinProfile) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().update(profile)
        }

    fun deleteProfile(profile: InsulinProfile) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().delete(profile)
        }.execute()
}

