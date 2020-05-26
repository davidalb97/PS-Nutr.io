package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.db.dto.InsulinProfileDto
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class InsulinProfileRepository {

    fun getAllProfiles(): LiveData<List<InsulinProfileDto>> {
        return roomDb.insulinProfileDao().getAll()
    }

    fun getProfile(name: String): InsulinProfileDto {
        return roomDb.insulinProfileDao().get(name)
    }

    fun addProfile(profile: InsulinProfileDto) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().insert(profile)
        }.execute()

    fun updateProfile(profile: InsulinProfileDto) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().update(profile)
        }

    fun deleteProfile(profile: InsulinProfileDto) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().delete(profile)
        }.execute()
}

