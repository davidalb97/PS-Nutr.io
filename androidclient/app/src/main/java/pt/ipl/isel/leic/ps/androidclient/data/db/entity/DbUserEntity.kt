package pt.ipl.isel.leic.ps.androidclient.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = DbUserEntity.tableName)
data class DbUserEntity(
    @PrimaryKey val userId: Int,
    val name: String
) {
    companion object {
        const val tableName = "DbUser"
    }
}