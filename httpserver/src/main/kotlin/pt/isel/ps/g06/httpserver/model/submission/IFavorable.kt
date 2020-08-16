package pt.isel.ps.g06.httpserver.model.submission

import pt.isel.ps.g06.httpserver.model.Submitter

interface IFavorable {
    val favorable: Favorable
}

class Favorable(private val getFavorite: (Submitter) -> Boolean) {
    private val userFavorites: MutableMap<Submitter, Boolean> = mutableMapOf()

    fun getUserFavorite(submitter: Submitter): Boolean {
        return userFavorites.computeIfAbsent(submitter, getFavorite)
    }
}