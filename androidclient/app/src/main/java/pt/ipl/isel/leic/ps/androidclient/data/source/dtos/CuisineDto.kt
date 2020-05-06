package pt.ipl.isel.leic.ps.androidclient.data.source.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class CuisineDto(val name: String)

class CuisinesDto(@JsonProperty("cuisines") val cuisineDtoList: MutableList<CuisineDto>)