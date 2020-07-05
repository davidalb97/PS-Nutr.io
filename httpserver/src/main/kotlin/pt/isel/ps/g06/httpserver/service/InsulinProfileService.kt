package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.dataAccess.db.mapper.InsulinProfileMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.InsulinProfileDbRepository
import pt.isel.ps.g06.httpserver.model.InsulinProfile

@Service
class InsulinProfileService(
        private val insulinProfileDbRepository: InsulinProfileDbRepository
) {

    private val insulinProfileMapper = InsulinProfileMapper()

    fun getAllProfilesFromUser(submitterId: Int): Collection<InsulinProfile> =
            (insulinProfileDbRepository.getAllFromUser(submitterId).map { insulinProfileMapper.mapToModel(it) })
                    .toList()


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
    ) {
        val insulinProfile = DbUserInsulinProfileDto(
                submitterId,
                profileName,
                startTime,
                endTime,
                glucoseObjective,
                insulinSensitivityFactor,
                carbohydrateRatio
        )
        insulinProfileDbRepository.insertProfile(insulinProfile)
    }

    fun deleteProfile(submitterId: Int, profileName: String) {
        insulinProfileDbRepository.deleteProfile(submitterId, profileName)
    }

}