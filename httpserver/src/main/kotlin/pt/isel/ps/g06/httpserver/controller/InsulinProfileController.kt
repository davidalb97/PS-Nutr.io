package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.AUTH_HEADER
import pt.isel.ps.g06.httpserver.common.INSULIN_PROFILE
import pt.isel.ps.g06.httpserver.common.INSULIN_PROFILES
import pt.isel.ps.g06.httpserver.common.PROFILE_NAME_VALUE
import pt.isel.ps.g06.httpserver.dataAccess.input.InsulinProfileInput
import pt.isel.ps.g06.httpserver.dataAccess.output.InsulinProfileOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toInsulinProfileOutput
import pt.isel.ps.g06.httpserver.service.AuthenticationService
import pt.isel.ps.g06.httpserver.service.InsulinProfileService
import pt.isel.ps.g06.httpserver.service.UserService

@Controller
class InsulinProfileController(
        private val insulinProfileService: InsulinProfileService,
        private val userService: UserService,
        private val authenticationService: AuthenticationService
) {
    @GetMapping(INSULIN_PROFILES)
    fun getAllUserInsulinProfiles(
            @RequestHeader(AUTH_HEADER) jwt: String
    ): ResponseEntity<Collection<InsulinProfileOutput>> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))
        return ResponseEntity.ok(
                insulinProfileService.getAllProfilesFromUser(submitterId)
                        .map(::toInsulinProfileOutput)
                        .toList()
        )
    }

    @GetMapping(INSULIN_PROFILE)
    fun getUserInsulinProfile(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(PROFILE_NAME_VALUE) profileName: String
    ): ResponseEntity<InsulinProfileOutput> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))

        return ResponseEntity.ok(
                toInsulinProfileOutput(insulinProfileService.getProfileFromUser(submitterId, profileName)
                )
        )
    }

    @PostMapping(INSULIN_PROFILES)
    fun createInsulinProfile(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @RequestBody insulinProfile: InsulinProfileInput
    ): ResponseEntity<Any> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))

        val profile = insulinProfileService.createProfile(
                submitterId,
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
            @RequestHeader(AUTH_HEADER) jwt: String,
            @PathVariable(PROFILE_NAME_VALUE) profileName: String
    ): ResponseEntity<*> {
        val submitterId = userService.getSubmitterIdFromUserName(authenticationService.getUsernameByJwt(jwt))
        return ResponseEntity.ok(
                insulinProfileService.deleteProfile(submitterId, profileName)
        )
    }
}