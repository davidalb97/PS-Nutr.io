package pt.isel.ps.g06.httpserver.service

import org.springframework.stereotype.Service
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.InsulinProfileDbRepository

@Service
class InsulinProfileService(
        val insulinProfileDbRepository: InsulinProfileDbRepository
) {

    fun getAllProfilesFromUser(submitterId: Int) {

    }

    fun getProfileFromUser(submitterId: Int, profileName: String) {

    }

    fun createProfile() {
        TODO()
    }

    fun deleteProfile() {
        TODO()
    }

}