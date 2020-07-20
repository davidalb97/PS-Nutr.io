package pt.isel.ps.g06.httpserver.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import pt.isel.ps.g06.httpserver.common.AUTH_HEADER
import pt.isel.ps.g06.httpserver.common.BEARER
import pt.isel.ps.g06.httpserver.common.INSULIN_PROFILES
import pt.isel.ps.g06.httpserver.common.PROFILE_NAME_VALUE
import pt.isel.ps.g06.httpserver.model.InsulinProfile
import pt.isel.ps.g06.httpserver.model.RequestError
import pt.isel.ps.g06.httpserver.security.JwtUtil
import pt.isel.ps.g06.httpserver.security.MyUserDetailsService
import pt.isel.ps.g06.httpserver.service.InsulinProfileService

@Controller
class InsulinProfileController(
        @Autowired private val insulinProfileService: InsulinProfileService,
        @Autowired private val userDetailsService: MyUserDetailsService,
        @Autowired private val jwtUtil: JwtUtil
) {
    @GetMapping(INSULIN_PROFILES)
    fun getAllUserInsulinProfiles(
            @RequestHeader(AUTH_HEADER) jwt: String
    ): ResponseEntity<Collection<InsulinProfile>> {
        val submitterId = getSubmitterIdFromJwt(jwt)
        return ResponseEntity.ok(insulinProfileService.getAllProfilesFromUser(submitterId))
    }

    @GetMapping(INSULIN_PROFILES, params = [PROFILE_NAME_VALUE])
    fun getUserInsulinProfile(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @RequestParam(PROFILE_NAME_VALUE) profileName: String
    ): ResponseEntity<*> {
        val submitterId = getSubmitterIdFromJwt(jwt)

        return try {
            ResponseEntity.ok(insulinProfileService.getProfileFromUser(submitterId, profileName))
        } catch (e: Exception) {
            // TODO - Better exception handling
            ResponseEntity(
                    RequestError(404, "You don't have any profile called '$profileName'"),
                    HttpStatus.NOT_FOUND
            )
        }
    }

    @PostMapping(INSULIN_PROFILES)
    fun createInsulinProfile(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @RequestBody insulinProfile: InsulinProfile
    ): ResponseEntity<*> {
        val submitterId = getSubmitterIdFromJwt(jwt)

        if (submitterId != insulinProfile.submitterId) {
            return ResponseEntity(
                    RequestError(401, "Unauthorized"),
                    HttpStatus.UNAUTHORIZED
            )
        }

        // TODO - created
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

    @DeleteMapping(INSULIN_PROFILES)
    fun deleteInsulinProfile(
            @RequestHeader(AUTH_HEADER) jwt: String,
            @RequestParam(PROFILE_NAME_VALUE) profileName: String
    ): ResponseEntity<*> {
        val submitterId = getSubmitterIdFromJwt(jwt)
        return try {
            ResponseEntity.ok(insulinProfileService.deleteProfile(submitterId, profileName))
        } catch (e: Exception) {
            // TODO - Better exception handling
            ResponseEntity(
                    RequestError(404, "You don't have any profile called '$profileName'"),
                    HttpStatus.NOT_FOUND
            )
        }
    }

    private fun getSubmitterIdFromJwt(jwt: String): Int {
        // Extract username from sent token using the server secret
        val username = jwtUtil.getUsername(jwt.removePrefix(BEARER))

        // Get submitterId from the obtained username
        val submitterByUsername = userDetailsService.getSubmitterByUsername(username)

        return submitterByUsername.submitter_id
    }
}