package pt.isel.ps.g06.httpserver.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.ps.g06.httpserver.db.IDbRepository
import pt.isel.ps.g06.httpserver.util.ArrayDataClass

@RestController
@RequestMapping("/ingredients")
class IngredientsController(val db: IDbRepository) {

    @GetMapping(produces = ["application/json"])
    fun getIngredientsHandler(skip: Int?, count: Int?)
            = ArrayDataClass(db.getIngredients(skip?: 0, count?: 30))
            //= db.getIngredients(skip?: 0, count?: 30)
}