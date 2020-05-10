package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Handle
import org.junit.jupiter.api.Assertions
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.exceptions.TestParameterException

class RepoAsserts(val const: Constants) {

    fun assertSubmissionContract(handle: Handle, submissionId: Int, expectedContracts: List<SubmissionContractType>) {
        assertSubmissionContract(handle, listOf(submissionId), expectedContracts)
    }
    fun assertSubmissionContract(handle: Handle, submissionIds: List<Int>, expectedContracts: List<SubmissionContractType>) {
        val expectedContractNames = expectedContracts.map { it.toString() }.sorted()
        submissionIds.forEach {  submissionId ->
            val resultConstantNames = handle.attach(SubmissionContractDao::class.java)
                    .getAllById(submissionId)
                    .map { it.submission_contract }
                    .sorted()
            Assertions.assertEquals(expectedContractNames, resultConstantNames)
        }
    }

    fun assertSubmissionContractInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.submissionContractDtos.count()
        val newCount = handle.attach(SubmissionContractDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertSubmissionSubmitter(
            handle: Handle,
            expectedSubmissionId: Int,
            expectedSubmitterId: Int
    ) {
        assertSubmissionSubmitter(handle, listOf(expectedSubmissionId), expectedSubmitterId)
    }
    fun assertSubmissionSubmitter(
            handle: Handle,
            expectedSubmissionIds: List<Int>,
            expectedSubmitterId: Int
    ) {
        expectedSubmissionIds.forEach { expectedSubmissionId ->
            val submissionSubmitterDto = handle.attach(SubmissionSubmitterDao::class.java)
                    .getAllBySubmissionId(expectedSubmissionId)
            Assertions.assertTrue(submissionSubmitterDto.any {
                it.submitter_id == expectedSubmitterId
            })
        }
    }

    fun assertSubmissionSubmitterInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.submissionSubmitterDtos.count()
        val newCount = handle.attach(SubmissionSubmitterDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertSubmission(handle: Handle,
                         expectedSubmissionId: Int,
                         expectedSubmissionType: SubmissionType
    ) {
        assertSubmission(handle, listOf(expectedSubmissionId), expectedSubmissionType)
    }

    fun assertSubmission(handle: Handle,
                         expectedSubmissionIds: List<Int>,
                         expectedSubmissionType: SubmissionType
    ) {
        expectedSubmissionIds.forEach { resultSubmissionId ->
            val submissionDto = handle.attach(SubmissionDao::class.java)
                    .getById(resultSubmissionId)
            //Assert existence
            Assertions.assertNotNull(submissionDto)
            Assertions.assertEquals(expectedSubmissionType.toString(), submissionDto!!.submission_type)
        }
    }

    fun assertSubmissionInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.submissionDtos.count()
        val newCount = handle.attach(SubmissionDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertApiSubmission(handle: Handle,
                            expectedApiSubmissionIds: List<Int>,
                            apiSubmitterId: Int,
                            submissionType: SubmissionType,
                            apiIds: List<String>
    ) {
        val resultApiSubmissionIds = handle.attach(ApiSubmissionDao::class.java)
                .getAllBySubmitterIdSubmissionTypeAndApiIds(
                        apiSubmitterId,
                        submissionType.toString(),
                        apiIds
                ).map { it.submission_id }
        Assertions.assertNotNull(resultApiSubmissionIds)
        Assertions.assertTrue(expectedApiSubmissionIds.containsAll(resultApiSubmissionIds))
    }

    fun assertApiSubmissionInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.insertedApiSubmissionDtos.count()
        val newCount = handle.attach(ApiSubmissionDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertMeal(it: Handle, expectedSubmissionId: Int, expectedMealName: String) {
        val mealDto = it.attach(MealDao::class.java)
                .getById(expectedSubmissionId)
        Assertions.assertNotNull(mealDto)
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

    fun assertIngredient(handle: Handle,
                         expectedIngredientSubmissionIds: List<Int>,
                         expectedIngredientNames: List<String>
    ) {
        if(expectedIngredientSubmissionIds.size != expectedIngredientNames.size) {
            throw TestParameterException("expectedIngredientSubmissionIds size \""
                    + expectedIngredientSubmissionIds.size +
                    "\" is different from expectedIngredientNames \"" +
                    + expectedIngredientNames.size +
                    "\" !")
        }
        val existingIngredientDtos = handle.attach(IngredientDao::class.java)
                .getAll()
        val filtered = existingIngredientDtos
                .filter { expectedIngredientSubmissionIds.contains(it.submission_id) }
                //.filter { expectedIngredientNames.contains(it.ingredient_name) }
        Assertions.assertEquals(expectedIngredientNames.size, filtered.size)
    }

    fun assertIngredientInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.ingredientDtos.count()
        val newCount = handle.attach(IngredientDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertMealIngredient(handle: Handle,
                             expectedMealSubmissionId: Int,
                             expectedIngredientSubmissionIds: List<Int>
    ) {
        val existingIngredientSubmissionIds = handle.attach(MealIngredientDao::class.java)
                .getAllByMealId(expectedMealSubmissionId)
                .map { it.ingredient_submission_id }

        //Assert that old ingredients were used on this meal
        Assertions.assertTrue(existingIngredientSubmissionIds.containsAll(expectedIngredientSubmissionIds))
    }

    fun assertMealIngredientInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.ingredientDtos.count()
        val newCount = handle.attach(MealIngredientDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }
}
