package pt.isel.ps.g06.httpserver.db.repo

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.REPORTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.VOTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.db.InsertConstants
import pt.isel.ps.g06.httpserver.db.RepoAsserts
import pt.isel.ps.g06.httpserver.db.inRollbackTransaction

@SpringBootTest
class MealSubmissionTest {

    @Autowired
    lateinit var jdbi: Jdbi

    @Autowired
    lateinit var mealRepo: MealDbRepository

    @Autowired
    lateinit var const: InsertConstants
    val asserts by lazy {
        RepoAsserts(const)
    }

    @Test
    fun shouldInsertUserMealWithExistingSpoonacularIngredients() {
        jdbi.inRollbackTransaction {
            val expectedSubmitterId = const.userDtos.first().submitter_id
            val expectedMealName = "Debug meal"
            val expectedMealApiId = null
            val expectedCuisines = const.cuisineNames.take(4)
            val expectedFoodApiType = FoodApiType.Spoonacular

            val expectedApiSubmitter = const.firstFoodApi
            val expectedIngredients = const.ingredients
                    .filter { it.apiType == FoodApiType.valueOf(expectedApiSubmitter.submitter_name) }
                    .take(3)

            val insertedMealDto = mealRepo.insert(
                    expectedSubmitterId,
                    expectedMealName,
                    expectedMealApiId,
                    expectedCuisines,
                    expectedIngredients,
                    expectedFoodApiType
            )

            val expectedSubmissionId = const.nextSubmissionId
            val expectedMealContracts = listOf(VOTABLE, REPORTABLE)

            //Assert Submission insertions
            asserts.assertSubmission(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedSubmissionType = SubmissionType.MEAL,
                    resultSubmissionId = insertedMealDto.submission_id
            )
            asserts.assertSubmissionInsertCount(it, 1)

            //Assert SubmissionContract VOTABLE, REPORTABLE contracts on submission
            asserts.assertSubmissionContract(it, insertedMealDto.submission_id, expectedMealContracts)
            asserts.assertSubmissionContractInsertCount(it, expectedMealContracts.size)

            //Assert SubmissionSubmitter insertions
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedSubmitterId = expectedSubmitterId,
                    resultSubmissionId = insertedMealDto.submission_id
            )
            asserts.assertSubmissionSubmitterCount(it, 1)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedMealName = expectedMealName,
                    resultSubmissionId = insertedMealDto.submission_id
            )
            asserts.assertMealInsertCount(it, 1)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisines = expectedCuisines,
                    resultSubmissionId = insertedMealDto.submission_id
            )
            asserts.assertMealCuisinesInsertCount(it, expectedCuisines.size)

            //Assert no ApiSubmission insertions (meal does not have apiId, ingredients already inserted)
            asserts.assertApiSubmissionInsertCount(it, 0)

            //Assert no Ingredient insertions (ingredients already inserted)
            asserts.assertIngredientInsertCount(it, 0)

            //Assert MealCuisine insertions (also counts)
            asserts.assertMealIngredient(it,
                    expectedApiSubmitterDto = expectedApiSubmitter,
                    existingIngredient = expectedIngredients,
                    expectedIngredients = expectedIngredients,
                    expectedIngredientInsertCount = 0,
                    resultSubmissionId = insertedMealDto.submission_id
            )
        }
    }
}