package pt.ipl.isel.leic.ps.androidclient.data.api.dto

import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo

class MealDto(
    val identifier: String,
    val name: String,
    val image_url: String,
    val info: MealInfo
)