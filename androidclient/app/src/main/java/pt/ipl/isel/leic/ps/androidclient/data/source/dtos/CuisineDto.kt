package pt.ipl.isel.leic.ps.androidclient.data.source.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine

@JsonIgnoreProperties(ignoreUnknown = true)
data class CuisineDto(val name: String) : IUnDto<Cuisine> {
    override fun unDto(): Cuisine = Cuisine(name)
}

data class CuisinesDto(
    @JsonProperty("cuisines") val cuisines: Array<CuisineDto>
) : IUnDto<List<Cuisine>> {

    override fun equals(other: Any?): Boolean {

        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CuisinesDto

        if (!cuisines.contentEquals(other.cuisines)) return false

        return true
    }

    override fun hashCode(): Int {
        return cuisines.contentHashCode()
    }

    override fun unDto(): List<Cuisine> = cuisines.map(CuisineDto::unDto)
}