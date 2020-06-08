package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfileEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.DbInsulinProfileMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class InsulinProfileRepository {

    val insulinProfileMapper = DbInsulinProfileMapper()

    fun getAllProfiles(): LiveData<List<DbInsulinProfileEntity>> =
        roomDb.insulinProfileDao().getAll()

    fun getProfile(name: String): LiveData<DbInsulinProfileEntity> =
        roomDb.insulinProfileDao().get(name)

    fun addProfile(profileDb: InsulinProfile) = AsyncWorker<Unit, Unit> {
        roomDb.insulinProfileDao().insert(insulinProfileMapper.mapToRelation(profileDb))
    }

    fun updateProfile(profileDb: InsulinProfile) = AsyncWorker<Unit, Unit> {
        roomDb.insulinProfileDao().update(insulinProfileMapper.mapToRelation(profileDb))
    }

    fun deleteProfile(profileDb: InsulinProfile) = AsyncWorker<Unit, Unit> {
        roomDb.insulinProfileDao().delete(insulinProfileMapper.mapToRelation(profileDb))
    }
}

