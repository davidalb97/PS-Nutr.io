package pt.ipl.isel.leic.ps.androidclient.data.sources.dtos

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class MealDto {
}

class MealsDto (@JsonProperty("meals") val mealDtoList: MutableList<MealDto>)