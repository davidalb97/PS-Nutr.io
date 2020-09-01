package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.common.exception.problemJson.notFound.MissingInsulinProfileException
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.InsulinProfileResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.InsulinProfileDbRepository
import pt.isel.ps.g06.httpserver.model.InsulinProfile
import java.time.LocalTime
import java.util.stream.Stream

@Service
class InsulinProfileService(
        private val insulinProfileDbRepository: InsulinProfileDbRepository,
        private val insulinProfileMapper: InsulinProfileResponseMapper
) {

    fun getAllProfilesFromUser(submitterId: Int, count: Int?, skip: Int?): Stream<InsulinProfile> {
        return insulinProfileDbRepository.getAllFromUser(submitterId, count, skip)
                .map(insulinProfileMapper::mapTo)
    }

    fun getProfileFromUser(submitterId: Int, profileName: String): InsulinProfile {
        return insulinProfileDbRepository.getFromUser(submitterId, profileName)
                ?.let(insulinProfileMapper::mapTo)
                ?: throw MissingInsulinProfileException(profileName)
    }

    fun createProfile(
            submitterId: Int,
            profileName: String,
            startTime: LocalTime,
            endTime: LocalTime,
            glucoseObjective: Int,
            insulinSensitivityFactor: Int,
            carbohydrateRatio: Int
    ): InsulinProfile {
        return insulinProfileMapper.mapTo(
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
        //TODO Get insulin profile before deleting it
        insulinProfileDbRepository.deleteProfile(submitterId, profileName)
    }

}