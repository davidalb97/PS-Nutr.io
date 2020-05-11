package pt.ipl.isel.leic.ps.androidclient.data.source.dto.meal.preview

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class MealDto(
    val name: String,
    val apiId: Int,
    val apiTypeStr: String
)