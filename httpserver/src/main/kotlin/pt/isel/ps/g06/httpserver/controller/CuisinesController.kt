package pt.isel.ps.g06.httpserver.controller

import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.common.CUISINES
import pt.isel.ps.g06.httpserver.dataAccess.output.CuisinesOutput

/*
@RestController
@RequestMapping(
        CUISINES,
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class CuisinesController(private val cuisinesService:) {


    @GetMapping
    fun getCuisinesHandler(
            @RequestParam skip: Int?,
            @RequestParam count: Int?
    ): ResponseEntity<CuisinesOutput> {

    }

}
*/