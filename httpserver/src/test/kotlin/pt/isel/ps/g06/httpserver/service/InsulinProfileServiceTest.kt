package pt.isel.ps.g06.httpserver.service

import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.*
import pt.isel.ps.g06.httpserver.anyNonNull
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbUserInsulinProfileDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.InsulinProfileDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.mapper.InsulinProfileResponseMapper
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.InvalidInsulinProfileTimesException
import pt.isel.ps.g06.httpserver.exception.problemJson.badRequest.OverlappingInsulinProfilesException
import pt.isel.ps.g06.httpserver.model.InsulinProfile
import java.time.LocalTime
import java.time.OffsetDateTime

class InsulinProfileServiceTest {
    private lateinit var repository: InsulinProfileDbRepository
    private lateinit var mapper: InsulinProfileResponseMapper
    private lateinit var service: InsulinProfileService


    @BeforeEach
    fun mockDependencies() {
        repository = mock(InsulinProfileDbRepository::class.java)
        mapper = mock(InsulinProfileResponseMapper::class.java)

        service = InsulinProfileService(
                insulinProfileDbRepository = repository,
                insulinProfileMapper = mapper
        )
    }

    @Test
    fun `inserting an insulin profile with intersecting time zones should throw an exception`() {
        val existingProfile = InsulinProfile(
                identifier = 1,
                startTime = LocalTime.MIN.toString(),
                endTime = LocalTime.MIN.plusHours(6).toString(),
                glucoseObjective = 100,
                insulinSensitivityFactor = 40,
                carbohydrateRatio = 12,
                modificationDate = OffsetDateTime.now(),
                name = "Profile 1"
        )

        val mock = mock(InsulinProfileService::class.java)
        `when`(mock.createProfile(
                submitterId = 1,
                profileName = "Profile 1",
                startTime = LocalTime.parse(existingProfile.startTime),
                endTime = LocalTime.parse(existingProfile.startTime).plusHours(1),
                glucoseObjective = 100,
                insulinSensitivityFactor = 40,
                carbohydrateRatio = 10
        )).thenCallRealMethod()

        `when`(mock.getAllProfilesFromUser(1, null, null)).thenReturn(sequenceOf(existingProfile))

        assertThrows<OverlappingInsulinProfilesException> {
            mock.createProfile(
                    submitterId = 1,
                    profileName = "Profile 1",
                    startTime = LocalTime.parse(existingProfile.startTime),
                    endTime = LocalTime.parse(existingProfile.startTime).plusHours(1),
                    glucoseObjective = 100,
                    insulinSensitivityFactor = 40,
                    carbohydrateRatio = 10
            )
        }
    }

    @Test
    fun `inserting an insulin profile with no intersecting time zones not should throw an exception`() {
        val submitterId = 10
        val existingProfile = InsulinProfile(
                identifier = submitterId,
                startTime = LocalTime.MIN.toString(),
                endTime = LocalTime.MIN.plusHours(1).toString(),
                glucoseObjective = 100,
                insulinSensitivityFactor = 40,
                carbohydrateRatio = 12,
                modificationDate = OffsetDateTime.now(),
                name = "Profile 1"
        )
        val profileDto = mock(DbUserInsulinProfileDto::class.java)

        `when`(repository.getAllFromUser(
                submitterId,
                null,
                null
        )).thenReturn(sequenceOf(profileDto))

        `when`(repository.insertProfile(
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull()
        )).thenReturn(profileDto)
        `when`(mapper.mapTo(profileDto)).thenReturn(existingProfile)

        service.createProfile(
                submitterId = submitterId,
                profileName = "Profile 2",
                startTime = LocalTime.parse(existingProfile.endTime).plusHours(1),
                endTime = LocalTime.parse(existingProfile.endTime).plusHours(2),
                glucoseObjective = 100,
                insulinSensitivityFactor = 40,
                carbohydrateRatio = 10
        )

        //See if repository is called once
        verify(repository).insertProfile(
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull()
        )
    }

    @Test
    fun `inserting an insulin profile with equal start and end time should throw an exception`() {
        val time = LocalTime.NOON

        assertThrows<InvalidInsulinProfileTimesException> {
            service.createProfile(
                    submitterId = 1,
                    profileName = "Profile 1",
                    startTime = time,
                    endTime = time,
                    glucoseObjective = 100,
                    insulinSensitivityFactor = 40,
                    carbohydrateRatio = 12
            )

            Assert.fail()
        }
    }

    @Test
    fun `inserting an insulin profile with end time happening before start time should throw an exception`() {
        val startTime = LocalTime.NOON
        val endTime = startTime.minusHours(1)

        assertThrows<InvalidInsulinProfileTimesException> {
            service.createProfile(
                    submitterId = 1,
                    profileName = "Profile 1",
                    startTime = startTime,
                    endTime = endTime,
                    glucoseObjective = 100,
                    insulinSensitivityFactor = 40,
                    carbohydrateRatio = 12
            )

            Assert.fail()
        }
    }
}