package pt.ipl.isel.leic.ps.androidclient.data.sources.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class CuisineDto {
}

class CuisinesDto(@JsonProperty("cuisines") val cuisineDtoList: MutableList<CuisineDto>)