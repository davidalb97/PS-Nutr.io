package pt.ipl.isel.leic.ps.androidclient.data.repo

import androidx.lifecycle.LiveData
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.roomDb
import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.InsulinProfileDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.InsulinProfileOutput
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.InputInsulinProfileMapper
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.InsulinProfileEntity
import pt.ipl.isel.leic.ps.androidclient.data.db.mapper.DbInsulinProfileMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.InsulinProfile
import pt.ipl.isel.leic.ps.androidclient.data.util.AsyncWorker

class InsulinProfileRepository(private val dataSource: InsulinProfileDataSource) {

    val insulinProfileMapper = DbInsulinProfileMapper()
    private val inputInsulinProfileMapper = InputInsulinProfileMapper()

    fun getAllDbProfiles(): LiveData<List<InsulinProfileEntity>> =
        roomDb.insulinProfileDao().getAll()

    fun getAllProfiles(
        jwt: String,
        onSuccess: (List<InsulinProfile>) -> Unit,
        onError: (VolleyError) -> Unit
    ) {

        dataSource.getAllInsulinProfiles(
            jwt,
            {
                onSuccess(inputInsulinProfileMapper.mapToListInputModel(it.asIterable()))
            },
            onError
        )
    }

    fun getDbProfile(name: String): LiveData<InsulinProfileEntity> =
        roomDb.insulinProfileDao().get(name)

    fun getProfile(
        jwt: String,
        name: String,
        onSuccess: (InsulinProfile) -> Unit,
        onError: (VolleyError) -> Unit
    ) {
        val insulinProfile = roomDb.insulinProfileDao().get(name)

        if (insulinProfile.value == null) {
            dataSource.getInsulinProfile(
                jwt,
                name,
                { onSuccess(inputInsulinProfileMapper.mapToInputModel(it)) },
                onError
            )
        } else {
            onSuccess(insulinProfileMapper.mapToModel(insulinProfile.value!!))
        }
    }

    fun addProfile(profileDb: InsulinProfile, jwt: String, onError: (VolleyError) -> Unit) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().insert(insulinProfileMapper.mapToRelation(profileDb))
            dataSource.postInsulinProfile(
                InsulinProfileOutput(
                    profileName = profileDb.profileName,
                    startTime = profileDb.startTime,
                    endTime = profileDb.endTime,
                    glucoseObjective = profileDb.glucoseObjective,
                    glucoseAmountPerInsulin = profileDb.glucoseAmountPerInsulin,
                    carbsAmountPerInsulin = profileDb.carbsAmountPerInsulin
                ),
                jwt,
                onError
            )
        }

    // TODO
    fun updateProfile(jwt: String, profileDb: InsulinProfile) = AsyncWorker<Unit, Unit> {
        roomDb.insulinProfileDao().update(insulinProfileMapper.mapToRelation(profileDb))
    }

    fun deleteProfile(profileName: String, jwt: String, onError: (VolleyError) -> Unit) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().delete(profileName)
            dataSource.deleteInsulinProfile(profileName, jwt, onError)
        }
}

