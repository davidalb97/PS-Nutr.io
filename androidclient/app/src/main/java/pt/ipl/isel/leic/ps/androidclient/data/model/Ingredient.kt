package pt.ipl.isel.leic.ps.androidclient.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ingredient")
data class Ingredient (
    @PrimaryKey val id: String,
    val name: String
)