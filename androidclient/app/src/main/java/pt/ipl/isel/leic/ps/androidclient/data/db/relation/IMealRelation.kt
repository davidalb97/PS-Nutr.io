package pt.ipl.isel.leic.ps.androidclient.data.db.relation

import pt.ipl.isel.leic.ps.androidclient.data.db.entity.DbIngredientEntity

interface IMealRelation<Entity>{

    fun fetchIngredients(): List<DbIngredientEntity>

    fun fetchMeal(): Entity
}