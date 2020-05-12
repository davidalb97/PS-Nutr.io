package pt.isel.ps.g06.httpserver.db.repo

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.*
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient
import pt.isel.ps.g06.httpserver.db.*
import pt.isel.ps.g06.httpserver.model.TestCuisine
import pt.isel.ps.g06.httpserver.model.TestIngredient
import pt.isel.ps.g06.httpserver.springConfig.dto.DbEditableDto

@SpringBootTest
@TestMethodOrder(MethodOrderer.Random::class)
class MealSubmissionTest {

    @Autowired
    lateinit var jdbi: Jdbi
    @Autowired
    lateinit var mealRepo: MealDbRepository
    @Autowired
    lateinit var asserts: RepoAsserts
    @Autowired
    lateinit var dbEditableConfig: DbEditableDto
    @Autowired
    lateinit var const: Constants


    @Test
    fun shouldInsertUserMealWithoutIngredients() {
        jdbi.inSandbox(const) {

            val expectedSubmitterId = const.userDtos.first().submitter_id
            val expectedMealName = "Debug meal"
            val expectedMealApiId = "5000"
            val expectedCuisines = const.cuisineDtos.take(4)
            val expectedApiSubmitter = const.firstFoodApi
            val expectedFoodApiType = FoodApiType.valueOf(expectedApiSubmitter.submitter_name)
            val expectedSubmissionId = const.nextSubmissionId
            val expectedMealContracts = listOf(VOTABLE, REPORTABLE, API)

            val insertedMealDto = mealRepo.insert(
                    submitterId = expectedSubmitterId,
                    mealName = expectedMealName,
                    apiId = expectedMealApiId,
                    cuisineNames = expectedCuisines.map { it.cuisine_name },
                    foodApi = expectedFoodApiType
            )

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

            //Assert SubmissionSubmitter insertions (User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = listOf(expectedSubmissionId),
                    expectedSubmitterId = expectedSubmitterId
            )
            //Assert SubmissionSubmitter insertions (API)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = listOf(expectedSubmissionId),
                    expectedSubmitterId = expectedApiSubmitter.submitter_id
            )
            //Assert SubmissionSubmitter insertion count (User + API)
            asserts.assertSubmissionSubmitterInsertCount(it, 2)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedMealName = expectedMealName
            )
            asserts.assertMealInsertCount(it, 1)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = expectedCuisines.map { it.cuisine_id },
                    resultSubmissionId = insertedMealDto.submission_id
            )
            asserts.assertMealCuisinesInsertCount(it, expectedCuisines.size)


            //Assert no ApiSubmission insertions (meal does not have apiId, ingredients already inserted)
            asserts.assertApiSubmission(it,
                    expectedApiSubmissionIds = listOf(expectedSubmissionId),
                    apiSubmitterId = expectedApiSubmitter.submitter_id,
                    submissionType = SubmissionType.MEAL,
                    apiIds = listOf(expectedMealApiId)
            )
            asserts.assertApiSubmissionInsertCount(it, 1)

            //Assert no Ingredient insertions (no ingredients specified as meal is from API)
            asserts.assertIngredientInsertCount(it, 0)

            //Assert no MealCuisine insert count (no ingredients specified as meal is from API)
            asserts.assertMealIngredientInsertCount(it, 0)
        }
    }

    @Test
    fun shouldInsertUserMealWithExistingSpoonacularIngredients() {
        jdbi.inSandbox(const) {
            val expectedSubmitterId = const.userDtos.first().submitter_id
            val expectedMealName = "Debug meal"
            val expectedMealApiId = null
            val expectedCuisines = const.cuisineDtos.take(4)
            val expectedApiSubmitter = const.firstFoodApi
            val expectedFoodApiType = FoodApiType.valueOf(expectedApiSubmitter.submitter_name)
            val expectedIngredientCount = 3
            val expectedIngredients = const.ingredients
                    .filter { it.foodApi.apiType == expectedFoodApiType }
                    .take(expectedIngredientCount)

            val insertedMealDto = mealRepo.insert(
                    expectedSubmitterId,
                    expectedMealName,
                    expectedMealApiId,
                    expectedCuisines.map { it.cuisine_name },
                    expectedIngredients.map(TestIngredient::toModel),
                    expectedFoodApiType
            )

            val expectedSubmissionId = const.nextSubmissionId
            val expectedMealContracts = listOf(VOTABLE, REPORTABLE)
//            val expectedIngredientSubmissionIds = getDtosFromIngredientNames(
//                    it,
//                    expectedApiSubmitter.submitter_id,
//                    expectedIngredients.map { it.name }
//            ).map { it.submission_id }
            val expectedIngredientSubmissionIds = expectedIngredients.map { it.submissionId }

            //Assert Submission insertions (Meal)
            asserts.assertSubmission(it,
                    expectedSubmissionId = expectedSubmissionId,
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
            asserts.assertSubmissionSubmitterInsertCount(it, 1)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedMealName = expectedMealName
            )
            asserts.assertMealInsertCount(it, 1)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = expectedCuisines.map { it.cuisine_id },
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
        jdbi.inSandbox(const) {
            val expectedSubmitterId = const.userDtos.first().submitter_id
            val expectedMealName = "Debug meal"
            val expectedMealApiId = null
            val expectedCuisines = const.cuisineDtos.take(4)
            val expectedApiSubmitter = const.firstFoodApi
            val expectedFoodApiType = FoodApiType.valueOf(expectedApiSubmitter.submitter_name)
            val expectedSubmissionId = const.nextSubmissionId
            val expectedIngredientCount = 3
            val expectedIngredientSubmissionIds =
                    ((expectedSubmissionId + 1)..(expectedSubmissionId + expectedIngredientCount))
                            .toList()
            val expectedIngredients = expectedIngredientSubmissionIds
                    .map { Ingredient("Test Ingredient$it", "-$it", expectedFoodApiType) }

            val insertedMealDto = mealRepo.insert(
                    expectedSubmitterId,
                    expectedMealName,
                    expectedMealApiId,
                    expectedCuisines.map { it.cuisine_name },
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
            asserts.assertSubmissionSubmitterInsertCount(it, 1 + expectedIngredientCount)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedMealName = expectedMealName
            )
            asserts.assertMealInsertCount(it, 1)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = expectedCuisines.map { it.cuisine_id },
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

//    @Test
//    fun shouldUpdateUserMealWithNewSpoonacularIngredients() {
//
//    }
//
//    @Test
//    fun shouldNotUpdateApiMeal() {
//        Assertions.assertThrows(InvalidInputException::class.java) {
//            TODO()
//        }
//    }


//    private fun getFirstMealWithIngredients(handle: Handle): TestMeal {
//        //Existing meal with ingredients
//        val existingMealId = const.submissionDtos
//                //Only meal submissions
//                .filter { it.submission_type == SubmissionType.MEAL.toString() }
//                .map { it.submission_id }
//                //Meal with ingredients
//                .first { mealId -> const.mealIngredienteDtos.any { it.meal_submission_id == mealId } }
//        return const.mealDtos.first { it.submission_id == existingMealId }
//                .mapToTest(handle)
//    }

    @Test
    fun shouldUpdateUserMealWithNewIngredientsAndCuisinesPreservingOldValues() {
        jdbi.inSandbox(const) {
            val existingMeal = const.meals.first { it.ingredients.isNotEmpty() && it.cuisines.isNotEmpty()}
            val expectedName = "TestNewMealName"
            val newIngredientCount = 3
            val newIngredientIds =
                (const.nextSubmissionId until const.nextSubmissionId + newIngredientCount)
                        .toList()
            val newIngredients = newIngredientIds.map { TestIngredient(
                    "TestIngredient$it",
                    it,
                    "TestApiId$it",
                    existingMeal.foodApi
            ) }
            val expectedIngredients = existingMeal.ingredients.union(newIngredients).toList()
            val expectedIngredientIds = expectedIngredients.map { it.submissionId }
            val newCuisineCount = 3
            val newCuisines = const.cuisineDtos.filter { cuisineDto ->
                existingMeal.cuisines.none { it.cuisineId == cuisineDto.cuisine_id }
            }.take(newCuisineCount)
            val expectedCuisines = existingMeal.cuisines.map(TestCuisine::toCuisineDto)
                    .union(newCuisines).toList()
//            val existingIngredients =
//
//            val expectedMealName = "Debug meal"
//            val expectedMealApiId = null
//            val expectedCuisines = const.cuisineNames.take(4)
//            val expectedApiSubmitter = const.firstFoodApi
//            val expectedFoodApiType = FoodApiType.valueOf(expectedApiSubmitter.submitter_name)
//            val expectedSubmissionId = const.nextSubmissionId
//            val expectedIngredientCount = 3
//            val expectedIngredientSubmissionIds =
//                    ((expectedSubmissionId + 1)..(expectedSubmissionId + expectedIngredientCount))
//                            .toList()
//            val expectedIngredients = expectedIngredientSubmissionIds
//                    .map { Ingredient("Test Ingredient$it", it * (-1), expectedFoodApiType) }

            mealRepo.update(
                    existingMeal.submitterId,
                    existingMeal.submissionId,
                    expectedName,
                    expectedCuisines.map { it.cuisine_name },
                    expectedIngredients.map(TestIngredient::toModel)
            )

            val expectedMealContracts = listOf(VOTABLE, REPORTABLE)
            val expectedIngredientContracts = listOf(API)

            //Assert current Meal submissions existence
            asserts.assertSubmission(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmissionType = SubmissionType.MEAL
            )
            //Assert current Ingredient submissions existence (Ingredient old + new, without removed)
            asserts.assertSubmission(it,
                    expectedSubmissionIds = expectedIngredientIds,
                    expectedSubmissionType = SubmissionType.INGREDIENT
            )
            //Assert Submission insertion count (new ingredients)
            asserts.assertSubmissionInsertCount(it, newIngredientIds.size)

            //Assert SubmissionContract API contracts on meal submission
            asserts.assertSubmissionContract(it,
                    submissionId = existingMeal.submissionId,
                    expectedContracts = expectedMealContracts
            )
            //Assert SubmissionContract API contracts on ingredient submissions
            asserts.assertSubmissionContract(it,
                    submissionIds = expectedIngredientIds,
                    expectedContracts = expectedIngredientContracts
            )
            //Assert SubmissionContract API contracts insert counts 1 for each ingredient
            asserts.assertSubmissionContractInsertCount(it,
                    expectedIngredientContracts.size * newIngredientIds.size
            )

            //Assert SubmissionSubmitter insertions (Meal)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.submitterId
            )
            //Assert SubmissionSubmitter insertions (Ingredient)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = expectedIngredientIds,
                    expectedSubmitterId = existingMeal.foodApi.submitterId
            )
            asserts.assertSubmissionSubmitterInsertCount(it, newIngredientIds.size)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedMealName = existingMeal.mealName
            )
            asserts.assertMealInsertCount(it, 0)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = expectedCuisines.map { it.cuisine_id },
                    resultSubmissionId = existingMeal.submissionId
            )
            asserts.assertMealCuisinesInsertCount(it, newCuisineCount)

            //Assert Ingredient ApiSubmission insertions (meal does not have apiId, ingredients not inserted)
            asserts.assertApiSubmission(it,
                    expectedApiSubmissionIds = expectedIngredientIds,
                    apiSubmitterId = existingMeal.foodApi.submitterId,
                    submissionType = SubmissionType.INGREDIENT,
                    apiIds = expectedIngredients.map { it.apiId }
            )
            asserts.assertApiSubmissionInsertCount(it, newIngredientIds.size)

            //Assert Ingredient existence
            asserts.assertIngredient(it,
                    expectedIngredientSubmissionIds = expectedIngredientIds,
                    expectedIngredientNames = expectedIngredients.map { it.name }
            )
            //Assert Ingredient insertion count
            asserts.assertIngredientInsertCount(it, newIngredientIds.size)

            //Assert MealCuisine existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = existingMeal.submissionId,
                    expectedIngredientSubmissionIds = expectedIngredientIds
            )
            //Assert MealCuisine insert count
            asserts.assertMealIngredientInsertCount(it, newIngredientIds.size)
        }
    }


}
