package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.db.IDbRepository

@RestController
@RequestMapping("/cuisines")
class CuisinesController(val db: IDbRepository) {

    @GetMapping(produces = ["application/json"])
    fun getCuisinesHandler(skip: Int?, count: Int?) = CuisinesResponse(db.getCuisines(skip?: 0, count?: 30))
}

data class CuisinesResponse(val cuisines: Array<String>) {

    //Deafult impl
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CuisinesResponse

        if (!cuisines.contentEquals(other.cuisines)) return false

        return true
    }

    //Deafult impl
    override fun hashCode(): Int {
        return cuisines.contentHashCode()
    }
}