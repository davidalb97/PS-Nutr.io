package pt.isel.ps.g06.httpserver.model.modular

import pt.isel.ps.g06.httpserver.model.Cuisine

interface ICuisines {
    val cuisines: Sequence<Cuisine>
}