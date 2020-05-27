package pt.ipl.isel.leic.ps.androidclient.data.api.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class CuisineDto(val name: String)

