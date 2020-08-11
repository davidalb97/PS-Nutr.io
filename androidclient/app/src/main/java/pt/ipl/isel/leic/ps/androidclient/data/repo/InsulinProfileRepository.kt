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
import pt.ipl.isel.leic.ps.androidclient.hasInternetConnection

class InsulinProfileRepository(private val dataSource: InsulinProfileDataSource) {

    val insulinProfileMapper = DbInsulinProfileMapper()
    private val inputInsulinProfileMapper = InputInsulinProfileMapper()

    fun getAllProfiles() = roomDb.insulinProfileDao().getAll()

    fun getRemoteProfiles() {
        /*if (!jwt.isNullOrBlank() && hasInternetConnection()) {
            dataSource.getAllInsulinProfiles(
                jwt,
                {
                    onSuccess(inputInsulinProfileMapper.mapToListInputModel(it.asIterable()))
                },
                onError
            )
        }*/
    }

    /*fun getProfile(
        jwt: String?,
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
    }*/

    fun addProfile(profileDb: InsulinProfile, jwt: String?, onError: (VolleyError) -> Unit) =
        AsyncWorker<Unit, Unit> {
            roomDb.insulinProfileDao().insert(insulinProfileMapper.mapToRelation(profileDb))

            if (!jwt.isNullOrBlank() && hasInternetConnection()) {
                dataSource.postInsulinProfile(
                    InsulinProfileOutput(
                        profileName = profileDb.profileName,
                        startTime = profileDb.startTime,
                        endTime = profileDb.endTime,
                        glucoseObjective = profileDb.glucoseObjective,
                        insulinSensitivityFactor = profileDb.glucoseAmountPerInsulin,
                        carbohydrateRatio = profileDb.carbsAmountPerInsulin
                    ),
                    jwt,
                    onError
                )
            }
        }

    fun deleteProfile(profileName: String, jwt: String?, onError: (VolleyError) -> Unit) =
        AsyncWorker<Unit, Unit> {
            if (!jwt.isNullOrBlank() && hasInternetConnection())
                dataSource.deleteInsulinProfile(profileName, jwt, onError)
            roomDb.insulinProfileDao().delete(profileName)
        }
}

