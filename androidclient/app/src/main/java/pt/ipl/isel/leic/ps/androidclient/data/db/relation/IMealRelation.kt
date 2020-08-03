package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbComponentIngredientEntity

interface IMealRelation<Entity> {

    fun fetchIngredients(): List<DbComponentIngredientEntity>

    fun fetchMeal(): Entity
}