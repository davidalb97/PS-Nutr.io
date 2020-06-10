package pt.isel.ps.g06.httpserver.db.repo

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.API
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.FAVORABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient
import pt.isel.ps.g06.httpserver.db.*
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException
import pt.isel.ps.g06.httpserver.model.TestCuisine
import pt.isel.ps.g06.httpserver.model.TestIngredient
import pt.isel.ps.g06.httpserver.springConfig.dto.DbEditableDto
import java.time.Clock
import java.time.Duration
import java.time.OffsetDateTime

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
    fun `should insert Api meal without ingredients`() {
        jdbi.inSandbox(const) {

            val expectedSubmitterId = const.userDtos.first().submitter_id
            val expectedMealName = "Debug meal"
            val expectedMealApiId = "5000"
            val expectedCuisines = const.cuisineDtos.take(4)
            val expectedApiSubmitter = const.firstFoodApi
            val expectedFoodApiType = FoodApiType.valueOf(expectedApiSubmitter.submitter_name)
            val expectedSubmissionId = const.nextSubmissionId
            val expectedMealContracts = listOf(API, FAVORABLE)

            val insertedMealDto = mealRepo.insert(
                    submitterId = expectedSubmitterId,
                    mealName = expectedMealName,
                    apiId = expectedMealApiId,
                    cuisineNames = expectedCuisines.map { it.cuisine_name },
                    foodApi = expectedFoodApiType
            )

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

            //Assert SubmissionSubmitter insertions (Meal, User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedSubmitterId = expectedSubmitterId
            )
            //Assert SubmissionSubmitter insertions (Meal, Api)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = expectedSubmissionId,
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
                    expectedCuisineIds = expectedCuisines.map { it.submission_id },
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
    fun `should insert user meal with existing Api ingredients`() {
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
            val expectedMealContracts = listOf(API, FAVORABLE)
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

            //Assert SubmissionSubmitter insertions (Meal, User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedSubmitterId = expectedSubmitterId
            )
            //Assert SubmissionSubmitter insertions (Meal, Api)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedSubmitterId = expectedApiSubmitter.submitter_id
            )
            asserts.assertSubmissionSubmitterInsertCount(it, 2)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedMealName = expectedMealName
            )
            asserts.assertMealInsertCount(it, 1)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = expectedCuisines.map { it.submission_id },
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

            //Assert MealIngredient existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = expectedSubmissionId,
                    expectedIngredientSubmissionIds = expectedIngredientSubmissionIds
            )
            //Assert MealIngredient insert count
            asserts.assertMealIngredientInsertCount(it, expectedIngredientSubmissionIds.size)
        }
    }

    @Test
    fun `should insert user meal without existing Api ingredients`() {
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


            val expectedMealContracts = listOf(API, FAVORABLE)

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

            //Assert SubmissionSubmitter insertions (Meal, User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedSubmitterId = expectedSubmitterId
            )
            //Assert SubmissionSubmitter insertions (Meal, Api)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedSubmitterId = expectedApiSubmitter.submitter_id
            )
            //Assert SubmissionSubmitter insertions (Ingredient)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = expectedIngredientSubmissionIds,
                    expectedSubmitterId = expectedApiSubmitter.submitter_id
            )
            asserts.assertSubmissionSubmitterInsertCount(it, 2 + expectedIngredientCount)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = expectedSubmissionId,
                    expectedMealName = expectedMealName
            )
            asserts.assertMealInsertCount(it, 1)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = expectedCuisines.map { it.submission_id },
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

            //Assert MealIngredient existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = expectedSubmissionId,
                    expectedIngredientSubmissionIds = expectedIngredientSubmissionIds
            )
            //Assert MealIngredient insert count
            asserts.assertMealIngredientInsertCount(it, expectedIngredientSubmissionIds.size)
        }
    }

    @Test
    fun `shouldn't insert with invalid cuisines names`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val expectedSubmitterId = const.userDtos.first().submitter_id
                val expectedMealName = "Debug meal"
                val expectedMealApiId = "5000"
                val invalidCuisines = generateSequence(0) { it + 1 }
                        .map { "TestCuisine$it" }
                        .filter { testCuisine -> const.cuisineNames.none { it == testCuisine } }
                        .take(4)
                val expectedApiSubmitter = const.firstFoodApi
                val expectedFoodApiType = FoodApiType.valueOf(expectedApiSubmitter.submitter_name)

                mealRepo.insert(
                        submitterId = expectedSubmitterId,
                        mealName = expectedMealName,
                        apiId = expectedMealApiId,
                        cuisineNames = invalidCuisines.toList(),
                        foodApi = expectedFoodApiType
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.CUISINE, exception.domain)
    }

    @Test
    fun `should update user meal with new name, ingredients & Cuisines preserving old values`() {
        jdbi.inSandbox(const) {
            val existingMeal = const.meals.first { it.ingredients.isNotEmpty() && it.cuisines.isNotEmpty() }
            val expectedName = "TestNewMealName"
            val newIngredientCount = 3
            val newIngredientIds =
                    (const.nextSubmissionId until const.nextSubmissionId + newIngredientCount)
                            .toList()
            val newIngredients = newIngredientIds.map {
                TestIngredient(
                        "TestIngredient$it",
                        it,
                        OffsetDateTime.now(),
                        "TestApiId$it",
                        existingMeal.foodApi
                )
            }
            val expectedIngredients = existingMeal.ingredients.union(newIngredients).toList()
            val expectedIngredientIds = expectedIngredients.map { it.submissionId }
            val newCuisineCount = 3
            val newCuisines = const.cuisineDtos.filter { cuisineDto ->
                existingMeal.cuisines.none { it.cuisineId == cuisineDto.submission_id }
            }.take(newCuisineCount)
            val expectedCuisines = existingMeal.cuisines.map(TestCuisine::toCuisineDto)
                    .union(newCuisines).toList()
            val expectedMealContracts = listOf(API, FAVORABLE)
            val expectedIngredientContracts = listOf(API)

            //Bypass time restriction
            bypassSubmissionEditLock(it, existingMeal.submissionId)

            mealRepo.update(
                    existingMeal.submitterId,
                    existingMeal.submissionId,
                    expectedName,
                    expectedCuisines.map { it.cuisine_name },
                    expectedIngredients.map(TestIngredient::toModel)
            )

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

            //Assert SubmissionSubmitter insertions (Meal, User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.submitterId
            )
            //Assert SubmissionSubmitter insertions (Meal, Api)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.foodApi.submitterId
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
                    expectedMealName = expectedName
            )
            asserts.assertMealInsertCount(it, 0)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = expectedCuisines.map { it.submission_id },
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

            //Assert MealIngredient existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = existingMeal.submissionId,
                    expectedIngredientSubmissionIds = expectedIngredientIds
            )
            //Assert MealIngredient insert count
            asserts.assertMealIngredientInsertCount(it, newIngredientIds.size)
        }
    }

    @Test
    fun `should update user meal with new name & ingredients preserving old values`() {
        jdbi.inSandbox(const) {
            val existingMeal = const.meals.first {
                it.ingredients.isNotEmpty() &&
                        it.cuisines.isNotEmpty() &&
                        it.restaurantMeals.isNotEmpty()
            }
            val expectedName = "TestNewMealName"
            val newIngredientCount = 3
            val newIngredientIds =
                    (const.nextSubmissionId until const.nextSubmissionId + newIngredientCount)
                            .toList()
            val newIngredients = newIngredientIds.map {
                TestIngredient(
                        "TestIngredient$it",
                        it,
                        OffsetDateTime.now(),
                        "TestApiId$it",
                        existingMeal.foodApi
                )
            }
            val expectedIngredients = existingMeal.ingredients.union(newIngredients).toList()
            val expectedIngredientIds = expectedIngredients.map { it.submissionId }
            val expectedMealContracts = listOf(API, FAVORABLE)
            val expectedIngredientContracts = listOf(API)

            //Bypass time restriction
            bypassSubmissionEditLock(it, existingMeal.submissionId)

            mealRepo.update(
                    existingMeal.submitterId,
                    existingMeal.submissionId,
                    expectedName,
                    existingMeal.cuisines.map { it.cuisineName },
                    expectedIngredients.map(TestIngredient::toModel)
            )

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

            //Assert SubmissionSubmitter insertions (Meal, User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.submitterId
            )
            //Assert SubmissionSubmitter insertions (Meal, Api)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.foodApi.submitterId
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
                    expectedMealName = expectedName
            )
            asserts.assertMealInsertCount(it, 0)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = existingMeal.cuisines.map { it.cuisineId },
                    resultSubmissionId = existingMeal.submissionId
            )
            asserts.assertMealCuisinesInsertCount(it, 0)

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

            //Assert MealIngredient existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = existingMeal.submissionId,
                    expectedIngredientSubmissionIds = expectedIngredientIds
            )
            //Assert MealIngredient insert count
            asserts.assertMealIngredientInsertCount(it, newIngredientIds.size)
        }
    }

    @Test
    fun `should update user meal with new name & cuisines preserving old values`() {
        jdbi.inSandbox(const) {
            val existingMeal = const.meals.first { it.ingredients.isNotEmpty() && it.cuisines.isNotEmpty() }
            val expectedName = "TestNewMealName"
            val newCuisineCount = 3
            val newCuisines = const.cuisineDtos.filter { cuisineDto ->
                existingMeal.cuisines.none { it.cuisineId == cuisineDto.submission_id }
            }.take(newCuisineCount)
            val expectedCuisines = existingMeal.cuisines.map(TestCuisine::toCuisineDto)
                    .union(newCuisines).toList()
            val existingMealIngredientIds = existingMeal.ingredients.map { it.submissionId }
            val expectedMealContracts = listOf(API, FAVORABLE)
            val expectedIngredientContracts = listOf(API)

            //Bypass time restriction
            bypassSubmissionEditLock(it, existingMeal.submissionId)

            mealRepo.update(
                    existingMeal.submitterId,
                    existingMeal.submissionId,
                    expectedName,
                    expectedCuisines.map { it.cuisine_name },
                    existingMeal.ingredients.map(TestIngredient::toModel)
            )

            //Assert current Meal submissions existence
            asserts.assertSubmission(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmissionType = SubmissionType.MEAL
            )
            //Assert current Ingredient submissions existence (Ingredient old + new, without removed)
            asserts.assertSubmission(it,
                    expectedSubmissionIds = existingMealIngredientIds,
                    expectedSubmissionType = SubmissionType.INGREDIENT
            )
            //Assert Submission insertion count (new ingredients)
            asserts.assertSubmissionInsertCount(it, 0)

            //Assert SubmissionContract API contracts on meal submission
            asserts.assertSubmissionContract(it,
                    submissionId = existingMeal.submissionId,
                    expectedContracts = expectedMealContracts
            )
            //Assert SubmissionContract API contracts on ingredient submissions
            asserts.assertSubmissionContract(it,
                    submissionIds = existingMealIngredientIds,
                    expectedContracts = expectedIngredientContracts
            )
            //Assert SubmissionContract API contracts insert count
            asserts.assertSubmissionContractInsertCount(it, 0)

            //Assert SubmissionSubmitter insertions (Meal, User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.submitterId
            )
            //Assert SubmissionSubmitter insertions (Meal, Api)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.foodApi.submitterId
            )
            //Assert SubmissionSubmitter insertions (Ingredient)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = existingMealIngredientIds,
                    expectedSubmitterId = existingMeal.foodApi.submitterId
            )
            asserts.assertSubmissionSubmitterInsertCount(it, 0)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedMealName = expectedName
            )
            asserts.assertMealInsertCount(it, 0)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = expectedCuisines.map { it.submission_id },
                    resultSubmissionId = existingMeal.submissionId
            )
            asserts.assertMealCuisinesInsertCount(it, newCuisineCount)

            //Assert Ingredient ApiSubmission insertions (meal does not have apiId, ingredients not inserted)
            asserts.assertApiSubmission(it,
                    expectedApiSubmissionIds = existingMealIngredientIds,
                    apiSubmitterId = existingMeal.foodApi.submitterId,
                    submissionType = SubmissionType.INGREDIENT,
                    apiIds = existingMeal.ingredients.map { it.apiId }
            )
            asserts.assertApiSubmissionInsertCount(it, 0)

            //Assert Ingredient existence
            asserts.assertIngredient(it,
                    expectedIngredientSubmissionIds = existingMealIngredientIds,
                    expectedIngredientNames = existingMeal.ingredients.map { it.name }
            )
            //Assert Ingredient insertion count
            asserts.assertIngredientInsertCount(it, 0)

            //Assert MealIngredient existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = existingMeal.submissionId,
                    expectedIngredientSubmissionIds = existingMealIngredientIds
            )
            //Assert MealIngredient insert count
            asserts.assertMealIngredientInsertCount(it, 0)
        }
    }

    @Test
    fun `shouldn't update from invalid submitter`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val existingMeal = const.meals.first()
                val invalidSubmitterId = const.submitterDtos.first {
                    it.submitter_id != existingMeal.submitterId
                            && existingMeal.foodApi.submitterId != it.submitter_id
                }.submitter_id
                val newName = "TestName"
                val expectedCuisines = emptyList<String>()
                val expectedIngredients = emptyList<Ingredient>()

                //Bypass time restriction
                bypassSubmissionEditLock(it, existingMeal.submissionId)

                mealRepo.update(
                        submissionId = existingMeal.submissionId,
                        submitterId = invalidSubmitterId,
                        name = newName,
                        cuisineNames = expectedCuisines,
                        ingredients = expectedIngredients
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.SUBMISSION_SUBMITTER, exception.domain)
    }

    @Test
    fun `shouldn't update from invalid submission`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val invalidSubmission = const.ingredients.first()
                val newName = "TestName"
                val expectedCuisines = emptyList<String>()
                val expectedIngredients = emptyList<Ingredient>()

                //Bypass time restriction
                bypassSubmissionEditLock(it, invalidSubmission.submissionId)

                mealRepo.update(
                        submissionId = invalidSubmission.submissionId,
                        submitterId = invalidSubmission.foodApi.submitterId,
                        name = newName,
                        cuisineNames = expectedCuisines,
                        ingredients = expectedIngredients
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.SUBMISSION, exception.domain)
    }

    @Test
    fun `shouldn't update from non-editable submission`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val currentTime = OffsetDateTime.now(Clock.systemDefaultZone())
                val timeout = dbEditableConfig.`edit-timeout-minutes`!!.seconds
                //First meal that is not possible to edit
                val existingMeal = const.meals.first {
                    Duration.between(it.date, currentTime).seconds > timeout
                }
                val newName = "TestName"
                val expectedCuisines = emptyList<String>()
                val expectedIngredients = emptyList<Ingredient>()
                mealRepo.update(
                        submissionId = existingMeal.submissionId,
                        submitterId = existingMeal.submitterId,
                        name = newName,
                        cuisineNames = expectedCuisines,
                        ingredients = expectedIngredients
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.TIMEOUT, exception.domain)
    }

    @Test
    fun `shouldn't update from api submission`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val existingMeal = const.meals.first { it.foodApiId != null }
                val newName = "TestName"
                val expectedCuisines = emptyList<String>()
                val expectedIngredients = emptyList<Ingredient>()

                //Bypass time restriction
                bypassSubmissionEditLock(it, existingMeal.submissionId)

                mealRepo.update(
                        submissionId = existingMeal.submissionId,
                        submitterId = existingMeal.submitterId,
                        name = newName,
                        cuisineNames = expectedCuisines,
                        ingredients = expectedIngredients
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.API, exception.domain)
    }

    @Test
    fun `shouldn't update with invalid cuisine names`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val existingMeal = const.meals.first { it.foodApiId != null }
                val newName = "TestName"
                val invalidCuisines = generateSequence(0) { it + 1 }
                        .map { "TestCuisine$it" }
                        .filter { testCuisine -> const.cuisineNames.none { it == testCuisine } }
                        .take(4)
                val expectedIngredients = emptyList<Ingredient>()

                //Bypass time restriction
                bypassSubmissionEditLock(it, existingMeal.submissionId)

                mealRepo.update(
                        submissionId = existingMeal.submissionId,
                        submitterId = existingMeal.submitterId,
                        name = newName,
                        cuisineNames = invalidCuisines.toList(),
                        ingredients = expectedIngredients
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.API, exception.domain)
    }

    @Test
    fun `should delete user meal with ingredients, cuisines, restaurants and portions`() {
        jdbi.inSandbox(const) {
            val existingMeal = getFirstTestMealWithCuisinesAndRestaurantPortions(const, false)
            val expectedIngredientIds = existingMeal.ingredients.map { it.submissionId }
            val expectedMealContracts = listOf(API, FAVORABLE)
            val expectedIngredientContracts = listOf(API)
            val expectedPortionDtos = existingMeal.restaurantMeals.flatMap { it.portions }

            //Bypass time restriction
            bypassSubmissionEditLock(it, existingMeal.submissionId)

            mealRepo.delete(existingMeal.submitterId, existingMeal.submissionId)

            //Assert current Meal submissions existence
            asserts.assertSubmission(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmissionType = SubmissionType.MEAL,
                    isDeleted = true
            )
            //Assert current Ingredient submissions existence (Ingredient old + new, without removed)
            asserts.assertSubmission(it,
                    expectedSubmissionIds = expectedIngredientIds,
                    expectedSubmissionType = SubmissionType.INGREDIENT,
                    isDeleted = false
            )
            //Assert Submission insertion count (deleted meal submission)
            asserts.assertSubmissionInsertCount(it, -1)

            //Assert SubmissionContract API contracts on meal submission
            asserts.assertSubmissionContract(it,
                    submissionId = existingMeal.submissionId,
                    expectedContracts = expectedMealContracts,
                    isDeleted = true
            )
            //Assert SubmissionContract API contracts on ingredient submissions
            asserts.assertSubmissionContract(it,
                    submissionIds = expectedIngredientIds,
                    expectedContracts = expectedIngredientContracts,
                    isDeleted = false
            )
            //Assert SubmissionContract meal API contracts delete count
            asserts.assertSubmissionContractInsertCount(it, expectedMealContracts.size * (-1))

            //Assert SubmissionSubmitter insertions (Meal, User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.submitterId,
                    isDeleted = true
            )
            //Assert SubmissionSubmitter insertions (Meal, Api)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.foodApi.submitterId,
                    isDeleted = true
            )
            //Assert SubmissionSubmitter insertions (Ingredient)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionIds = expectedIngredientIds,
                    expectedSubmitterId = existingMeal.foodApi.submitterId,
                    isDeleted = false
            )
            asserts.assertSubmissionSubmitterInsertCount(it, -2)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedMealName = existingMeal.mealName,
                    isDeleted = true
            )
            asserts.assertMealInsertCount(it, -1)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = existingMeal.cuisines.map { it.cuisineId },
                    resultSubmissionId = existingMeal.submissionId,
                    isDeleted = true
            )
            asserts.assertMealCuisinesInsertCount(it, existingMeal.cuisines.size * (-1))

            //Assert Ingredient ApiSubmission insertions (meal does not have apiId, ingredients not inserted)
            asserts.assertApiSubmission(it,
                    expectedApiSubmissionIds = expectedIngredientIds,
                    apiSubmitterId = existingMeal.foodApi.submitterId,
                    submissionType = SubmissionType.INGREDIENT,
                    apiIds = existingMeal.ingredients.map { it.apiId },
                    isDeleted = false
            )
            asserts.assertApiSubmissionInsertCount(it, 0)

            //Assert Ingredient existence
            asserts.assertIngredient(it,
                    expectedIngredientSubmissionIds = expectedIngredientIds,
                    expectedIngredientNames = existingMeal.ingredients.map { it.name },
                    isDeleted = false
            )
            //Assert Ingredient insertion count
            asserts.assertIngredientInsertCount(it, 0)

            //Assert MealIngredient existence
            asserts.assertMealIngredient(it,
                    expectedMealSubmissionId = existingMeal.submissionId,
                    expectedIngredientSubmissionIds = expectedIngredientIds,
                    isDeleted = true
            )
            //Assert MealIngredient insert count
            asserts.assertMealIngredientInsertCount(it, expectedIngredientIds.size * (-1))

            //Assert Portion existence
            asserts.assertPortion(it,
                    expectedPortionIds = expectedPortionDtos.map { it.submission_id },
                    expectedRestaurantMealIdsAndQuantities = expectedPortionDtos.map {
                        Pair(it.restaurant_meal_submission_id, it.quantity)
                    },
                    isDeleted = true
            )

            //Assert Portion insert count
            asserts.assertPortionInsertCount(it, expectedPortionDtos.size * (-1))

            //Assert RestaurantMeal existence
            asserts.assertRestaurantMeal(it,
                    expectedRestaurantMealIds = existingMeal.restaurantMeals.map { it.submissionId },
                    expectedRestaurantAndMealIds = existingMeal.restaurantMeals.map {
                        Pair(it.restaurantDto.submission_id, it.mealDto.submission_id)
                    },
                    isDeleted = true
            )
            //Assert MealIngredient insert count
            asserts.assertRestaurantMealInsertCount(it, existingMeal.restaurantMeals.size * (-1))
        }
    }

    @Test
    fun `should delete api meal with cuisines, restaurants and portions`() {
        jdbi.inSandbox(const) {
            val existingMeal = getFirstTestMealWithCuisinesAndRestaurantPortions(const, true)
            val expectedMealContracts = listOf(FAVORABLE, API)

            //Bypass time restriction
            bypassSubmissionEditLock(it, existingMeal.submissionId)

            mealRepo.delete(existingMeal.submitterId, existingMeal.submissionId)

            //Assert current Meal submissions existence
            asserts.assertSubmission(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmissionType = SubmissionType.MEAL,
                    isDeleted = true
            )
            //Assert Submission insertion count (deleted meal submission)
            asserts.assertSubmissionInsertCount(it, -1)

            //Assert SubmissionContract API contracts on meal submission
            asserts.assertSubmissionContract(it,
                    submissionId = existingMeal.submissionId,
                    expectedContracts = expectedMealContracts,
                    isDeleted = true
            )
            //Assert SubmissionContract meal API contracts delete count
            asserts.assertSubmissionContractInsertCount(it, expectedMealContracts.size * (-1))

            //Assert SubmissionSubmitter insertions (Meal, User)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.submitterId,
                    isDeleted = true
            )
            //Assert SubmissionSubmitter insertions (Meal, Api)
            asserts.assertSubmissionSubmitter(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedSubmitterId = existingMeal.foodApi.submitterId,
                    isDeleted = true
            )
            asserts.assertSubmissionSubmitterInsertCount(it, -2)

            //Assert Meal insertions
            asserts.assertMeal(it,
                    expectedSubmissionId = existingMeal.submissionId,
                    expectedMealName = existingMeal.mealName,
                    isDeleted = true
            )
            asserts.assertMealInsertCount(it, -1)

            //Assert MealCuisine insertions
            asserts.assertMealCuisines(it,
                    expectedCuisineIds = existingMeal.cuisines.map { it.cuisineId },
                    resultSubmissionId = existingMeal.submissionId,
                    isDeleted = true
            )
            asserts.assertMealCuisinesInsertCount(it, existingMeal.cuisines.size * (-1))

            //Assert RestaurantMeal existence
            asserts.assertRestaurantMeal(it,
                    expectedRestaurantMealIds = existingMeal.restaurantMeals.map { it.submissionId },
                    expectedRestaurantAndMealIds = existingMeal.restaurantMeals.map {
                        Pair(it.restaurantDto.submission_id, it.mealDto.submission_id)
                    },
                    isDeleted = true
            )
            //Assert RestaurantMeal insert count
            asserts.assertRestaurantMealInsertCount(it, existingMeal.restaurantMeals.size * (-1))
        }
    }

    @Test
    fun `shouldn't delete from invalid submitter`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val existingMeal = const.meals.first()
                val invalidSubmitterId = const.submitterDtos.first {
                    it.submitter_id != existingMeal.submitterId
                            && it.submitter_id != existingMeal.foodApi.submitterId
                }.submitter_id

                //Bypass time restriction
                bypassSubmissionEditLock(it, existingMeal.submissionId)

                mealRepo.delete(
                        submissionId = existingMeal.submissionId,
                        submitterId = invalidSubmitterId
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.SUBMISSION_SUBMITTER, exception.domain)
    }

    @Test
    fun `shouldn't delete from invalid submission`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val invalidSubmission = const.ingredients.first()

                //Bypass time restriction
                bypassSubmissionEditLock(it, invalidSubmission.submissionId)

                mealRepo.delete(
                        submissionId = invalidSubmission.submissionId,
                        submitterId = invalidSubmission.foodApi.submitterId
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.SUBMISSION, exception.domain)
    }

    @Test
    fun `shouldn't not delete from non editable submission`() {
        val exception = Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inSandbox(const) {
                val currentTime = OffsetDateTime.now(Clock.systemDefaultZone())
                val timeout = dbEditableConfig.`edit-timeout-minutes`!!.seconds
                //First meal that is not possible to edit
                val existingMeal = const.meals.first {
                    Duration.between(it.date, currentTime).seconds > timeout
                }
                mealRepo.delete(
                        submissionId = existingMeal.submissionId,
                        submitterId = existingMeal.submitterId
                )
            }
        }
        Assertions.assertEquals(InvalidInputDomain.TIMEOUT, exception.domain)
    }
}
