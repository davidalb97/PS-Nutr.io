package pt.isel.ps.g06.httpserver.model.modular

import pt.isel.ps.g06.httpserver.model.Cuisine
import java.util.stream.Stream

interface ICuisines {
    val cuisines: Stream<Cuisine>
}