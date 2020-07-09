package pt.isel.ps.g06.httpserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.INSULIN_PROFILE
import pt.isel.ps.g06.httpserver.common.INSULIN_PROFILES
import pt.isel.ps.g06.httpserver.common.PROFILE_NAME_VALUE
import pt.isel.ps.g06.httpserver.common.SUBMITTER_ID_VALUE
import pt.isel.ps.g06.httpserver.model.InsulinProfile
import pt.isel.ps.g06.httpserver.service.InsulinProfileService

@Controller
class InsulinProfileController(
        @Autowired private val insulinProfileService: InsulinProfileService
) {
    // TODO - needs to separate each operation for each user
    @GetMapping(INSULIN_PROFILES)
    fun getAllUserInsulinProfiles(
            @PathVariable(SUBMITTER_ID_VALUE) submitterId: Int
    ): ResponseEntity<Collection<InsulinProfile>> {

        return ResponseEntity.ok(insulinProfileService.getAllProfilesFromUser(submitterId))
    }

    @GetMapping(INSULIN_PROFILE)
    fun getUserInsulinProfile(
            @PathVariable(SUBMITTER_ID_VALUE) submitterId: Int,
            @PathVariable(PROFILE_NAME_VALUE) profileName: String
    ): ResponseEntity<InsulinProfile> {
        return ResponseEntity.ok(insulinProfileService.getProfileFromUser(submitterId, profileName))
    }

    @PostMapping(INSULIN_PROFILES)
    fun createInsulinProfile(
            @RequestBody insulinProfile: InsulinProfile
    ): ResponseEntity<*> {
        return ResponseEntity.ok(
                insulinProfileService.createProfile(
                        insulinProfile.submitterId,
                        insulinProfile.profileName,
                        insulinProfile.startTime,
                        insulinProfile.endTime,
                        insulinProfile.glucoseObjective,
                        insulinProfile.insulinSensitivityFactor,
                        insulinProfile.carbohydrateRatio
                )
        )
    }

    @DeleteMapping(INSULIN_PROFILE)
    fun deleteInsulinProfile(
            @PathVariable(SUBMITTER_ID_VALUE) submitterId: Int,
            @PathVariable(PROFILE_NAME_VALUE) profileName: String
    ): ResponseEntity<*> {
        return ResponseEntity.ok(insulinProfileService.deleteProfile(submitterId, profileName))
    }
}