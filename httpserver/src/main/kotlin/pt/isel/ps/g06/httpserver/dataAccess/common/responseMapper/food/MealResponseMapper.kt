package pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.food

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.ResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.restaurant.DbCuisineResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.submitter.DbSubmitterResponseMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CuisineDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmissionDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.SubmitterDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.MealComposition
import pt.isel.ps.g06.httpserver.model.food.Meal

@Component
class DbMealResponseMapper(
        private val ingredientMapper: IngredientResponseMapper,
        private val nutritionalMapper: DbNutritionalValuesResponseMapper,
        private val dbSubmitterMapper: DbSubmitterResponseMapper,
        private val dbCuisineMapper: DbCuisineResponseMapper,
        private val dbMealRepo: MealDbRepository,
        private val dbCuisineRepo: CuisineDbRepository,
        private val submissionDbRepository: SubmissionDbRepository,
        private val submitterDbRepository: SubmitterDbRepository
) : ResponseMapper<DbMealDto, Meal> {
    override fun mapTo(dto: DbMealDto): Meal {
        return Meal(
                identifier = dto.submission_id,
                name = dto.meal_name,
                nutritionalValues = nutritionalMapper.mapTo(dto),
                composedBy = MealComposition(
                        meals = dbMealRepo
                                .getMealComponents(dto.submission_id)
                                //TODO Right now meal component quantity is being ignored, we gotta fix that in model
                                .map { dbMealRepo.getById(it.meal_submission_id)!! }
                                .map(::mapTo),

                        ingredients = dbMealRepo
                                .getMealIngredients(dto.submission_id)
                                //TODO Right now meal Ingredient quantity is being ignored, we gotta fix that in model
                                .map { dbMealRepo.getById(it.meal_submission_id)!! }
                                .map(ingredientMapper::mapTo)
                ),
                cuisines = dbCuisineRepo.getAllByMealId(dto.submission_id).map(dbCuisineMapper::mapTo),
                creationDate = lazy { submissionDbRepository.getCreationDate(dto.submission_id) },
                submitterInfo = lazy {
                    submitterDbRepository
                            .getSubmitterForSubmission(dto.submission_id)
                            ?.let(dbSubmitterMapper::mapTo)
                }
        )
    }
}