package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.INSULIN_PROFILE
import pt.isel.ps.g06.httpserver.common.INSULIN_PROFILES
import pt.isel.ps.g06.httpserver.common.PROFILE_NAME_VALUE
import pt.isel.ps.g06.httpserver.common.exception.authentication.NotAuthenticatedException
import pt.isel.ps.g06.httpserver.dataAccess.input.InsulinProfileInput
import pt.isel.ps.g06.httpserver.dataAccess.output.InsulinProfileOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toInsulinProfileOutput
import pt.isel.ps.g06.httpserver.model.Submitter
import pt.isel.ps.g06.httpserver.service.InsulinProfileService

@Controller
class InsulinProfileController(private val insulinProfileService: InsulinProfileService) {

    @GetMapping(INSULIN_PROFILES)
    fun getAllUserInsulinProfiles(submitter: Submitter?): ResponseEntity<Collection<InsulinProfileOutput>> {
        submitter ?: throw NotAuthenticatedException()

        return ResponseEntity.ok(
                insulinProfileService
                        .getAllProfilesFromUser(submitter.identifier)
                        .map(::toInsulinProfileOutput)
                        .toList()
        )
    }

    @GetMapping(INSULIN_PROFILE)
    fun getUserInsulinProfile(
            @PathVariable(PROFILE_NAME_VALUE) profileName: String,
            submitter: Submitter?
    ): ResponseEntity<InsulinProfileOutput> {
        submitter ?: throw NotAuthenticatedException()

        val userProfile = insulinProfileService.getProfileFromUser(submitter.identifier, profileName)

        return ResponseEntity.ok(toInsulinProfileOutput(userProfile))
    }

    @PostMapping(INSULIN_PROFILES)
    fun createInsulinProfile(
            @RequestBody insulinProfile: InsulinProfileInput,
            submitter: Submitter?
    ): ResponseEntity<Void> {
        submitter ?: throw NotAuthenticatedException()

        val profile = insulinProfileService.createProfile(
                submitter.identifier,
                insulinProfile.profileName,
                insulinProfile.startTime,
                insulinProfile.endTime,
                insulinProfile.glucoseObjective,
                insulinProfile.insulinSensitivityFactor,
                insulinProfile.carbohydrateRatio
        )

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(INSULIN_PROFILE)
                        .buildAndExpand(mapOf(Pair(PROFILE_NAME_VALUE, profile.profileName)))
                        .toUri()
        ).build()
    }

    @DeleteMapping(INSULIN_PROFILE)
    fun deleteInsulinProfile(
            @PathVariable(PROFILE_NAME_VALUE) profileName: String,
            submitter: Submitter?
    ): ResponseEntity<Void> {
        submitter ?: throw NotAuthenticatedException()

        insulinProfileService.deleteProfile(submitter.identifier, profileName)

        return ResponseEntity
                .ok()
                .build()
    }
}