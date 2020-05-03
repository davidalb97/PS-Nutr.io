package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Handle
import org.junit.jupiter.api.Assertions
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.SubmitterDto
import pt.isel.ps.g06.httpserver.dataAccess.model.Ingredient

class RepoAsserts(val const: InsertConstants) {

    fun assertSubmissionContract(handle: Handle, resultSubmissionId: Int, expectedContracts: List<SubmissionContractType>) {
        val expectedContractNames = expectedContracts.map { it.toString() }.sorted()
        val resultConstantNames = handle.attach(SubmissionContractDao::class.java)
                .getAllById(resultSubmissionId)
                .map { it.submission_contract }
                .sorted()
        Assertions.assertEquals(expectedContractNames, resultConstantNames)
    }

    fun assertSubmissionContractInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.submissionContractDtos.count()
        val newCount = handle.attach(SubmissionContractDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertSubmissionSubmitter(
            handle: Handle,
            expectedSubmissionId: Int,
            expectedSubmitterId: Int,
            resultSubmissionId: Int
    ) {
        val submissionSubmitterDto = handle.attach(SubmissionSubmitterDao::class.java)
                .getBySubmissionId(resultSubmissionId)
        Assertions.assertNotNull(submissionSubmitterDto)
        Assertions.assertEquals(submissionSubmitterDto!!.submission_id, expectedSubmissionId)
        Assertions.assertEquals(submissionSubmitterDto.submitter_id, expectedSubmitterId)
    }

    fun assertSubmissionSubmitterCount(handle: Handle, insertCount: Int) {
        val oldCount = const.submissionSubmitterDtos.count()
        val newCount = handle.attach(SubmissionSubmitterDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertSubmission(handle: Handle,
                         expectedSubmissionId: Int,
                         expectedSubmissionType: SubmissionType,
                         resultSubmissionId: Int
    ) {
        val submissionDto = handle.attach(SubmissionDao::class.java)
                .getById(resultSubmissionId)
        Assertions.assertNotNull(submissionDto)
        Assertions.assertEquals(expectedSubmissionId, submissionDto!!.submission_id)
        Assertions.assertEquals(expectedSubmissionType.toString(), submissionDto.submission_type)
    }

    fun assertSubmissionInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.submissionDtos.count()
        val newCount = handle.attach(SubmissionDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertApiSubmissionInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.insertedApiSubmissionDtos.count()
        val newCount = handle.attach(ApiSubmissionDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertMeal(it: Handle, expectedSubmissionId: Int, expectedMealName: String, resultSubmissionId: Int) {
        val mealDto = it.attach(MealDao::class.java)
                .getById(resultSubmissionId)
        Assertions.assertNotNull(mealDto)
        Assertions.assertEquals(expectedSubmissionId, resultSubmissionId)
        Assertions.assertEquals(expectedMealName, mealDto!!.meal_name)
    }

    fun assertMealInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.mealDtos.count()
        val newCount = handle.attach(MealDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertMealCuisines(handle: Handle, expectedCuisines: List<String>, resultSubmissionId: Int) {
        val resultCuisines = handle.attach(MealCuisineDao::class.java)
                .getByMealId(resultSubmissionId)
                .map { it.cuisine_name }
                .sorted()
        Assertions.assertEquals(expectedCuisines.sorted(), resultCuisines)
    }

    fun assertMealCuisinesInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.mealCuisineDtos.count()
        val newCount = handle.attach(MealCuisineDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertIngredientInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.ingredientDtos.count()
        val newCount = handle.attach(IngredientDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertMealIngredient(handle: Handle,
                             expectedApiSubmitterDto: SubmitterDto,
                             existingIngredient: List<Ingredient>,
                             expectedIngredients: List<Ingredient>,
                             expectedIngredientInsertCount: Int,
                             resultSubmissionId: Int
    ) {
        val existingIngredients = zipToTestIngredient(
                const.apiSubmissionDtos,
                const.submissionSubmitterDtos,
                expectedApiSubmitterDto,
                existingIngredient
        )
        val currentIngredients = zipToTestIngredient(
                handle.attach(ApiSubmissionDao::class.java).getAll(),
                handle.attach(SubmissionSubmitterDao::class.java).getAll(),
                expectedApiSubmitterDto,
                expectedIngredients
        )
        //Assert insert count
        val oldCount = existingIngredients.count()
        val newCount = currentIngredients.count()
        Assertions.assertEquals(oldCount + expectedIngredientInsertCount, newCount)

        val currentIngredientSubmissionIds = handle.attach(MealIngredientDao::class.java)
                .getAllByMealId(resultSubmissionId)
                .map { it.ingredient_submission_id }

        //Assert that old ingredients were used on this meal
        Assertions.assertTrue(currentIngredientSubmissionIds.containsAll(existingIngredients.map { it.submissionId }))
        //Assert that old/new ingredients were used on this meal
        Assertions.assertTrue(currentIngredientSubmissionIds.containsAll(currentIngredients.map { it.submissionId }))
        //Assert that current ingredients contain
        Assertions.assertTrue(expectedIngredients.all { expectedIngredient ->
            currentIngredients.any {
                it.apiId == expectedIngredient.apiId
                        && it.apiName == expectedIngredient.apiType.toString()
            }
        })
    }
}
