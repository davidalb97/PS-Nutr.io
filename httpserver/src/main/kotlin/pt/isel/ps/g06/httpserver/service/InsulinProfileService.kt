package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.InsulinProfileDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.mapper.InsulinProfileResponseMapper
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.InvalidInsulinProfileTimesException
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.OverlappingInsulinProfilesException
import pt.isel.ps.g06.httpserver.exception.problemJson.notFound.MissingInsulinProfileException
import pt.isel.ps.g06.httpserver.model.InsulinProfile
import java.time.LocalTime

@Service
class InsulinProfileService(
        private val insulinProfileDbRepository: InsulinProfileDbRepository,
        private val insulinProfileMapper: InsulinProfileResponseMapper
) {

    fun getAllProfilesFromUser(submitterId: Int, count: Int?, skip: Int?): Sequence<InsulinProfile> {
        return insulinProfileDbRepository
                .getAllFromUser(submitterId, count, skip)
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
        if (startTime.hour >= endTime.hour) {
            throw InvalidInsulinProfileTimesException()
        }

        //See if dates overlap with active insulin profile
        val overlappingProfile = getAllProfilesFromUser(submitterId, null, null).find {
            val start = LocalTime.parse(it.startTime)
            val end = LocalTime.parse(it.endTime)

            (start.hour <= endTime.hour) && (end.hour >= startTime.hour)
        }

        if (overlappingProfile != null) {
            throw OverlappingInsulinProfilesException(overlappingProfile)
        }

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