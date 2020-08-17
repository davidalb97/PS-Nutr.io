package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.InsulinProfileResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.InsulinProfileDbRepository
import pt.isel.ps.g06.httpserver.model.InsulinProfile

@Service
class InsulinProfileService(
        private val insulinProfileDbRepository: InsulinProfileDbRepository,
        private val insulinProfileMapper: InsulinProfileResponseMapper
) {

    fun getAllProfilesFromUser(submitterId: Int): Sequence<InsulinProfile> {
        return insulinProfileDbRepository.getAllFromUser(submitterId)
                .map(insulinProfileMapper::mapToModel)
    }

    fun getProfileFromUser(submitterId: Int, profileName: String): InsulinProfile =
            insulinProfileMapper.mapToModel(insulinProfileDbRepository.getFromUser(submitterId, profileName))

    fun createProfile(
            submitterId: Int,
            profileName: String,
            startTime: String,
            endTime: String,
            glucoseObjective: Int,
            insulinSensitivityFactor: Int,
            carbohydrateRatio: Int
    ): InsulinProfile {
        return insulinProfileMapper.mapToModel(
                insulinProfileDbRepository.insertProfile(
                        submitterId,
                        profileName,
                        startTime,
                        endTime,
                        glucoseObjective,
                        insulinSensitivityFactor,
                        carbohydrateRatio
                )
        )
    }

    fun deleteProfile(submitterId: Int, profileName: String) {
        insulinProfileDbRepository.deleteProfile(submitterId, profileName)
    }

}