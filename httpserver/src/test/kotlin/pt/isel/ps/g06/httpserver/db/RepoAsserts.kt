package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Handle
import org.junit.jupiter.api.Assertions
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.*
import pt.isel.ps.g06.httpserver.exceptions.TestParameterException

class RepoAsserts(val const: Constants) {

    fun assertSubmissionContract(handle: Handle, submissionId: Int, expectedContracts: List<SubmissionContractType>, isDeleted: Boolean = false) {
        assertSubmissionContract(handle, listOf(submissionId), expectedContracts, isDeleted)
    }

    fun assertSubmissionContract(handle: Handle, submissionIds: List<Int>, expectedContracts: List<SubmissionContractType>, isDeleted: Boolean = false) {
        val expectedContractNames = expectedContracts.map { it.toString() }.sorted()
        submissionIds.forEach { submissionId ->
            val resultConstantNames = handle.attach(SubmissionContractDao::class.java)
                    .getAllById(submissionId)
                    .map { it.submission_contract }
                    .sorted()
            if (isDeleted) {
                Assertions.assertTrue(resultConstantNames.isEmpty())
            } else {
                Assertions.assertEquals(expectedContractNames, resultConstantNames)
            }
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
            expectedSubmitterId: Int,
            isDeleted: Boolean = false
    ) {
        assertSubmissionSubmitter(handle, listOf(expectedSubmissionId), expectedSubmitterId, isDeleted)
    }

    fun assertSubmissionSubmitter(
            handle: Handle,
            expectedSubmissionIds: List<Int>,
            expectedSubmitterId: Int,
            isDeleted: Boolean = false
    ) {
        expectedSubmissionIds.forEach { expectedSubmissionId ->
            val submissionSubmitterDto = handle.attach(SubmissionSubmitterDao::class.java)
                    .getAllBySubmissionId(expectedSubmissionId)
            if (isDeleted) {
                Assertions.assertTrue(submissionSubmitterDto.none {
                    it.submitter_id == expectedSubmitterId
                })
            } else {
                Assertions.assertTrue(submissionSubmitterDto.any {
                    it.submitter_id == expectedSubmitterId
                })
            }
        }
    }

    fun assertSubmissionSubmitterInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.submissionSubmitterDtos.count()
        val newCount = handle.attach(SubmissionSubmitterDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertSubmission(handle: Handle,
                         expectedSubmissionId: Int,
                         expectedSubmissionType: SubmissionType,
                         isDeleted: Boolean = false
    ) {
        assertSubmission(handle, listOf(expectedSubmissionId), expectedSubmissionType, isDeleted)
    }

    fun assertSubmission(handle: Handle,
                         expectedSubmissionIds: List<Int>,
                         expectedSubmissionType: SubmissionType,
                         isDeleted: Boolean = false
    ) {
        expectedSubmissionIds.forEach { resultSubmissionId ->
            val submissionDto = handle.attach(SubmissionDao::class.java)
                    .getById(resultSubmissionId)
            if (isDeleted) {
                Assertions.assertNull(submissionDto)
            } else {
                //Assert existence
                Assertions.assertNotNull(submissionDto)
                Assertions.assertEquals(expectedSubmissionType.toString(), submissionDto!!.submission_type)
            }
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
                            apiIds: List<String>,
                            isDeleted: Boolean = false
    ) {
        val resultApiSubmissionIds = handle.attach(ApiSubmissionDao::class.java)
                .getAllBySubmitterIdSubmissionTypeAndApiIds(
                        apiSubmitterId,
                        submissionType.toString(),
                        apiIds
                ).map { it.submission_id }
        if (isDeleted) {
            Assertions.assertTrue(resultApiSubmissionIds.isEmpty())
        } else {
            Assertions.assertFalse(resultApiSubmissionIds.isEmpty())
            Assertions.assertTrue(expectedApiSubmissionIds.containsAll(resultApiSubmissionIds))
        }
    }

    fun assertApiSubmissionInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.insertedApiSubmissionDtos.count()
        val newCount = handle.attach(ApiSubmissionDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertMeal(it: Handle, expectedSubmissionId: Int, expectedMealName: String, isDeleted: Boolean = false) {
        val mealDto = it.attach(MealDao::class.java)
                .getById(expectedSubmissionId)
        if (isDeleted) {
            Assertions.assertNull(mealDto)
        } else {
            Assertions.assertNotNull(mealDto)
            Assertions.assertEquals(expectedMealName, mealDto!!.meal_name)
        }
    }

    fun assertMealInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.mealDtos.count()
        val newCount = handle.attach(MealDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertMealCuisines(
            handle: Handle,
            expectedCuisineIds: List<Int>,
            resultSubmissionId: Int,
            isDeleted: Boolean = false
    ) {
        val resultCuisines = handle.attach(MealCuisineDao::class.java)
                .getAllByMealId(resultSubmissionId)
                .map { it.cuisine_submission_id }
                .sorted()
        if (isDeleted) {
            Assertions.assertTrue(resultCuisines.isEmpty())
        } else {
            Assertions.assertEquals(expectedCuisineIds.sorted(), resultCuisines)
        }
    }

    fun assertMealCuisinesInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.mealCuisineDtos.count()
        val newCount = handle.attach(MealCuisineDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertIngredient(handle: Handle,
                         expectedIngredientSubmissionIds: List<Int>,
                         expectedIngredientNames: List<String>,
                         isDeleted: Boolean = false
    ) {
        if (expectedIngredientSubmissionIds.size != expectedIngredientNames.size) {
            throw TestParameterException("expectedIngredientSubmissionIds size \""
                    + expectedIngredientSubmissionIds.size +
                    "\" is different from expectedIngredientNames \"" +
                    +expectedIngredientNames.size +
                    "\" !")
        }
        val existingIngredientDtos = handle.attach(IngredientDao::class.java)
                .getAll()
        val filtered = existingIngredientDtos
                .filter { expectedIngredientSubmissionIds.contains(it.submission_id) }
        //.filter { expectedIngredientNames.contains(it.ingredient_name) }
        if (isDeleted) {
            Assertions.assertTrue(filtered.isEmpty())
        } else {
            Assertions.assertEquals(expectedIngredientNames.size, filtered.size)
        }
    }

    fun assertIngredientInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.ingredientDtos.count()
        val newCount = handle.attach(IngredientDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertMealIngredient(handle: Handle,
                             expectedMealSubmissionId: Int,
                             expectedIngredientSubmissionIds: List<Int>,
                             isDeleted: Boolean = false
    ) {
        val existingIngredientSubmissionIds = handle.attach(MealIngredientDao::class.java)
                .getAllByMealId(expectedMealSubmissionId)
                .map { it.ingredient_submission_id }
        if (isDeleted) {
            Assertions.assertTrue(existingIngredientSubmissionIds.isEmpty())
        } else {
            //Assert that old ingredients were used on this meal
            Assertions.assertTrue(existingIngredientSubmissionIds
                    .containsAll(expectedIngredientSubmissionIds)
            )
        }
    }

    fun assertMealIngredientInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.ingredientDtos.count()
        val newCount = handle.attach(MealIngredientDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertPortion(handle: Handle,
                      expectedPortionIds: List<Int>,
                      expectedRestaurantMealIdsAndQuantities: List<Pair<Int, Int>> = emptyList(),
                      isDeleted: Boolean = false
    ) {
        expectedPortionIds.forEach { expectedPortionId ->
            val existingPortionDto = handle.attach(PortionDao::class.java)
                    .getById(expectedPortionId)
            if (isDeleted) {
                Assertions.assertNull(existingPortionDto)
            } else {
                if(expectedRestaurantMealIdsAndQuantities.isNotEmpty()) {
                    Assertions.assertNotNull(existingPortionDto)
                    Assertions.assertTrue(expectedRestaurantMealIdsAndQuantities.any { pair ->
                        existingPortionDto!!.restaurant_meal_submission_id == pair.first &&
                                existingPortionDto.quantity == pair.second
                    })
                }
            }
        }
    }

    fun assertPortionInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.portionDtos.count()
        val newCount = handle.attach(PortionDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }

    fun assertRestaurantMeal(handle: Handle,
                             expectedRestaurantMealIds: List<Int>,
                             expectedRestaurantAndMealIds: List<Pair<Int, Int>> = emptyList(),
                             isDeleted: Boolean = false
    ) {
        expectedRestaurantMealIds.forEach { expectedRestaurantMealId ->
            val existingRestaurantMealDto = handle.attach(RestaurantMealDao::class.java)
                    .getById(expectedRestaurantMealId)
            if (isDeleted) {
                Assertions.assertNull(existingRestaurantMealDto)
            } else {
                if(expectedRestaurantAndMealIds.isNotEmpty()) {
                    Assertions.assertNotNull(existingRestaurantMealDto)
                    Assertions.assertTrue(expectedRestaurantAndMealIds.any { pair ->
                        existingRestaurantMealDto!!.restaurant_submission_id == pair.first &&
                                existingRestaurantMealDto.meal_submission_id == pair.second
                    })
                }
            }
        }
    }

    fun assertRestaurantMealInsertCount(handle: Handle, insertCount: Int) {
        val oldCount = const.restaurantMealDtos.count()
        val newCount = handle.attach(RestaurantMealDao::class.java).getAll().count()
        Assertions.assertEquals(oldCount + insertCount, newCount)
    }
}
