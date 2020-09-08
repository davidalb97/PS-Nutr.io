package pt.isel.ps.g06.httpserver.springConfig.dto

import javax.validation.constraints.NotBlank

class DatabasePropertiesDto {
    @field:NotBlank
    lateinit var url: String

    @field:NotBlank
    lateinit var username: String

    @field:NotBlank
    lateinit var password: String

    @field:NotBlank
    lateinit var `driver-class-name`: String
}