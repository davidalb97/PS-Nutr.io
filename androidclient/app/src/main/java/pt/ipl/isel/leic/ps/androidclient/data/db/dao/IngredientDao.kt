package pt.ipl.isel.leic.ps.androidclient.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import pt.ipl.isel.leic.ps.androidclient.data.model.Ingredient

@Dao
interface IngredientDao {
    @Query("SELECT * FROM Ingredient")
    fun getAll(): LiveData<List<Ingredient>>

    @Update
    fun update(vararg ingredient: Ingredient)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg ingredient: Ingredient)

    @Query("SELECT * FROM Ingredient where id =:id")
    fun get(id: String?): Ingredient

    @Delete
    fun delete(ingredient: Ingredient)
}