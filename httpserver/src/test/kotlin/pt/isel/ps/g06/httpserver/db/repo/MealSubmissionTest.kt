package pt.isel.ps.g06.httpserver.db.repo

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.IngredientDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient
import pt.isel.ps.g06.httpserver.db.InsertConstants
import pt.isel.ps.g06.httpserver.db.RepoAsserts
import pt.isel.ps.g06.httpserver.db.getDtosFromIngredientNames
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
            val expectedApiSubmitter = const.firstFoodApi
            val expectedFoodApiType = FoodApiType.valueOf(expectedApiSubmitter.submitter_name)
            val expectedIngredientCount = 3
            val expectedIngredients = const.ingredients
                    .filter { it.apiType == expectedFoodApiType }
                    .take(expectedIngredientCount)

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
            val expectedIngredientSubmissionIds = getDtosFromIngredientNames(
                    it,
                    expectedApiSubmitter.submitter_id,
                    expectedIngredients.map { it.name }
                    ).map { it.submission_id }

            //Assert Submission insertions (Meal)
            asserts.assertSubmission(it,
                    expectedSubmissionIds = listOf(expectedSubmissionId),
                    expectedSubmissionType = SubmissionType.MEAL
            )
            asserts.assertSubmissionInsertCount(it, 1)

            //Assert SubmissionContract VOTABLE, REPORTABLE contracts on meal submission
            asserts.assertSubmissionContract(it,
                    submissionIds = listOf(insertedMealDto.submission_id),
                    expectedContracts = expectedMealContracts
            )
            asserts.assertSubmissionContractInsertCount(it, expectedMealContracts.size)

            //Assert SubmissionSubmitter insertions
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = listOf(expectedSubmissionId),
                    expectedSubmitterId = expectedSubmitterId
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

            //Assert Ingredient existence
            asserts.assertIngredient(it,
                    expectedIngredientSubmissionIds = expectedIngredientSubmissionIds,
                    expectedIngredientNames = expectedIngredients.map { it.name }
            )
            //Assert no Ingredient insertions (ingredients already inserted)
            asserts.assertIngredientInsertCount(it, 0)

            //Assert MealCuisine existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = expectedSubmissionId,
                    expectedIngredientSubmissionIds = expectedIngredientSubmissionIds
            )
            //Assert MealCuisine insert count
            asserts.assertMealIngredientInsertCount(it, expectedIngredientSubmissionIds.size)
        }
    }

    @Test
    fun shouldInsertUserMealWithoutExistingSpoonacularIngredients() {
        jdbi.inRollbackTransaction {
            val expectedSubmitterId = const.userDtos.first().submitter_id
            val expectedMealName = "Debug meal"
            val expectedMealApiId = null
            val expectedCuisines = const.cuisineNames.take(4)
            val expectedApiSubmitter = const.firstFoodApi
            val expectedFoodApiType = FoodApiType.valueOf(expectedApiSubmitter.submitter_name)
            val expectedSubmissionId = const.nextSubmissionId
            val expectedIngredientCount = 3
            val expectedIngredientSubmissionIds =
                    ((expectedSubmissionId + 1)..(expectedSubmissionId + expectedIngredientCount))
                            .toList()
            val expectedIngredients = expectedIngredientSubmissionIds
                    .map { Ingredient("Test Ingredient$it", it*(-1), expectedFoodApiType) }

            val insertedMealDto = mealRepo.insert(
                    expectedSubmitterId,
                    expectedMealName,
                    expectedMealApiId,
                    expectedCuisines,
                    expectedIngredients,
                    expectedFoodApiType
            )


            val expectedMealContracts = listOf(VOTABLE, REPORTABLE)

            //Assert Submission insertions (Meal)
            asserts.assertSubmission(it,
                    expectedSubmissionIds = listOf(expectedSubmissionId),
                    expectedSubmissionType = SubmissionType.MEAL
            )
            //Assert Submission insertions (Ingredient)
            asserts.assertSubmission(it,
                    expectedSubmissionIds = expectedIngredientSubmissionIds,
                    expectedSubmissionType = SubmissionType.INGREDIENT
            )
            //Assert Submission insertion count (meal + ingredients)
            asserts.assertSubmissionInsertCount(it, 1 + expectedIngredientCount)

            //Assert SubmissionContract VOTABLE, REPORTABLE contracts on meal submission
            asserts.assertSubmissionContract(it,
                    submissionIds = listOf(insertedMealDto.submission_id),
                    expectedContracts = expectedMealContracts
            )
            //Assert SubmissionContract API contracts on meal submission
            asserts.assertSubmissionContract(it,
                    submissionIds = expectedIngredientSubmissionIds,
                    expectedContracts = listOf(API)
            )
            //Assert SubmissionContract API contracts insert counts 2 for each meal, 1 for each ingredient
            asserts.assertSubmissionContractInsertCount(it,
                    expectedMealContracts.size + expectedIngredientSubmissionIds.size
            )

            //Assert SubmissionSubmitter insertions (Meal)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = listOf(expectedSubmissionId),
                    expectedSubmitterId = expectedSubmitterId
            )
            //Assert SubmissionSubmitter insertions (Ingredient)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = expectedIngredientSubmissionIds,
                    expectedSubmitterId = expectedApiSubmitter.submitter_id
            )
            asserts.assertSubmissionSubmitterCount(it, 1 + expectedIngredientCount)

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

            //Assert Ingredient ApiSubmission insertions (meal does not have apiId, ingredients not inserted)
            asserts.assertApiSubmission(it,
                    expectedApiSubmissionIds = expectedIngredientSubmissionIds,
                    apiSubmitterId = expectedApiSubmitter.submitter_id,
                    submissionType = SubmissionType.INGREDIENT,
                    apiIds = expectedIngredients.map { it.apiId }
            )
            asserts.assertApiSubmissionInsertCount(it, expectedIngredientCount)

            //Assert Ingredient existence
            asserts.assertIngredient(it,
                    expectedIngredientSubmissionIds = expectedIngredientSubmissionIds,
                    expectedIngredientNames = expectedIngredients.map { it.name }
            )
            //Assert Ingredient insertion count
            asserts.assertIngredientInsertCount(it, expectedIngredientCount)

            //Assert MealCuisine existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = expectedSubmissionId,
                    expectedIngredientSubmissionIds = expectedIngredientSubmissionIds
            )
            //Assert MealCuisine insert count
            asserts.assertMealIngredientInsertCount(it, expectedIngredientSubmissionIds.size)
        }
    }
}