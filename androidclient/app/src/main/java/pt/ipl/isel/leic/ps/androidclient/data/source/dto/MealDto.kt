package pt.ipl.isel.leic.ps.androidclient.data.source.dto

import pt.ipl.isel.leic.ps.androidclient.data.source.model.MealInfo

class MealDto(
    val identifier: String,
    val name: String,
    val image_url: String,
    val info: MealInfo
)