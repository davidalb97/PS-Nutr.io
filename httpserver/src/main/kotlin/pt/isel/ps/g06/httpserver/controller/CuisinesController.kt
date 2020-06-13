package pt.isel.ps.g06.httpserver.controller

/*
@RestController
@RequestMapping(
        CUISINES,
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
)
class CuisinesController(private val cuisinesService: CuisinesService) {


    @GetMapping
    fun getCuisinesHandler(
            @RequestParam skip: Int?,
            @RequestParam count: Int?
    ): ResponseEntity<CuisinesOutput> {
        val availableCuisines = cuisinesService.getAvailableCuisines(skip, count)

        return ResponseEntity
                .ok()
                .body(toSimplifiedCuisinesOutput(availableCuisines))
    }

}
*/