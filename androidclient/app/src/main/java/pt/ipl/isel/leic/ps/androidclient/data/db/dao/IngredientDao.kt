package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredient
import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbInsulinProfile

@Dao
interface IngredientDao {
    @Query("SELECT * FROM Ingredient")
    fun getAll(): LiveData<List<DbIngredient>>

    @Update
    fun update(vararg dbIngredient: DbIngredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg dbIngredient: DbIngredient)

    @Query("SELECT * FROM Ingredient where ingredientId = :id")
    fun get(id: String?): DbIngredient

    @Delete
    fun delete(dbIngredient: DbIngredient)

}