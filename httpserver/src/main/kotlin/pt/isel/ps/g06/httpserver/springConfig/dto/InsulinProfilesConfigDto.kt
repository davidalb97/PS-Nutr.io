package pt.isel.ps.g06.httpserver.springConfig.dto

import javax.validation.constraints.NotBlank

class InsulinProfilesConfigDto(@field:NotBlank var secret: String? = null)