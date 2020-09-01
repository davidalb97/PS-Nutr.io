package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import pt.isel.ps.g06.httpserver.common.*
import pt.isel.ps.g06.httpserver.dataAccess.input.insulin.InsulinProfileInput
import pt.isel.ps.g06.httpserver.dataAccess.output.InsulinProfileOutput
import pt.isel.ps.g06.httpserver.dataAccess.output.toInsulinProfileOutput
import pt.isel.ps.g06.httpserver.model.User
import pt.isel.ps.g06.httpserver.service.InsulinProfileService
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import kotlin.streams.toList

@Validated
@Suppress("MVCPathVariableInspection")
@Controller
class InsulinProfileController(private val insulinProfileService: InsulinProfileService) {

    @GetMapping(INSULIN_PROFILES)
    fun getAllUserInsulinProfiles(
            user: User,
            @RequestParam @Min(0) skip: Int?,
            @RequestParam(defaultValue = DEFAULT_COUNT_STR) @Min(0) @Max(MAX_COUNT) count: Int?
    ): ResponseEntity<Collection<InsulinProfileOutput>> =
            ResponseEntity.ok(
                    insulinProfileService
                            .getAllProfilesFromUser(user.identifier, count, skip)
                            .map(::toInsulinProfileOutput)
                            .toList()
            )


    @GetMapping(INSULIN_PROFILE)
    fun getUserInsulinProfile(
            @PathVariable(PROFILE_NAME_VALUE) profileName: String,
            user: User
    ): ResponseEntity<InsulinProfileOutput> {
        val userProfile = insulinProfileService.getProfileFromUser(user.identifier, profileName)

        return ResponseEntity.ok(toInsulinProfileOutput(userProfile))
    }

    @PostMapping(INSULIN_PROFILES)
    fun createInsulinProfile(
            @Valid @RequestBody insulinProfile: InsulinProfileInput,
            user: User
    ): ResponseEntity<Void> {
        val profile = insulinProfileService.createProfile(
                user.identifier,
                insulinProfile.profileName!!,
                insulinProfile.startTime!!,
                insulinProfile.endTime!!,
                insulinProfile.glucoseObjective!!,
                insulinProfile.insulinSensitivityFactor!!,
                insulinProfile.carbohydrateRatio!!
        )

        return ResponseEntity.created(
                UriComponentsBuilder
                        .fromUriString(INSULIN_PROFILE)
                        .buildAndExpand(mapOf(Pair(PROFILE_NAME_VALUE, profile.name)))
                        .toUri()
        ).build()
    }

    @DeleteMapping(INSULIN_PROFILE)
    fun deleteInsulinProfile(
            @PathVariable(PROFILE_NAME_VALUE) profileName: String,
            user: User
    ): ResponseEntity<Void> {

        insulinProfileService.deleteProfile(user.identifier, profileName)

        return ResponseEntity
                .ok()
                .build()
    }
}