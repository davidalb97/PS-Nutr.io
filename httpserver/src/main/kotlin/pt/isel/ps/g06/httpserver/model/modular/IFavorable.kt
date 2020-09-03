package pt.isel.ps.g06.httpserver.model.modular

interface IFavorable {
    //Useful for user's own custom meals published as restaurant meals
    val isFavorable: UserPredicate
    val isFavorite: UserPredicate
}