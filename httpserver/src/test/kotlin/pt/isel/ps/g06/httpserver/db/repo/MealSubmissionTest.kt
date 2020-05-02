package pt.isel.ps.g06.httpserver.db.repo

import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pt.isel.ps.g06.httpserver.dataAccess.api.food.FoodApiType
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.REPORTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType.VOTABLE
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionType
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.ApiSubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionContractDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionDao
import pt.isel.ps.g06.httpserver.dataAccess.db.dao.SubmissionSubmitterDao
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.db.inRollbackTransaction
import pt.isel.ps.g06.httpserver.exception.InvalidInputDomain
import pt.isel.ps.g06.httpserver.exception.InvalidInputException

@SpringBootTest
class MealSubmissionTest {

    @Autowired
    lateinit var jdbi: Jdbi

    @Autowired
    lateinit var mealRepo: MealDbRepository

    @Autowired
    lateinit var const: InsertConstants

    /**
     * Requires inserted user, ingredients
     */
    @Test
    fun shouldInsertUserMealWithExistingSpoonacularIngredients() {
        jdbi.inRollbackTransaction {

            val ingredients = const.insertedIngredients
                    .filter { it.apiType == FoodApiType.Spoonacular }
                    .take(3)

            val insertedMealDto = mealRepo.insert(
                    const.insertedUserDto.submitter_id,
                    "Debug meal",
                    null,
                    listOf(const.insertedCuisineNames[0]),
                    ingredients,
                    FoodApiType.Spoonacular
            )

            //Assert Submission
            val submissionDto = it.attach(SubmissionDao::class.java)
                    .getById(insertedMealDto.submission_id)
            Assertions.assertNotNull(submissionDto)
            Assertions.assertEquals(const.nextSubmissionId, submissionDto!!.submission_id)
            Assertions.assertEquals(SubmissionType.MEAL.toString(), submissionDto.submission_type)

            //Assert number of Submission insertions
            val oldSubssionCount = const.insertedSubmissions.count()
            val submissionsCount = it.attach(SubmissionDao::class.java).getAll().count()
            Assertions.assertEquals(oldSubssionCount + 1, submissionsCount)

            //Assert SubmissionSubmitter
            val submissionSubmitterDto = it.attach(SubmissionSubmitterDao::class.java)
                    .getBySubmissionId(insertedMealDto.submission_id)
            Assertions.assertNotNull(submissionSubmitterDto)
            Assertions.assertEquals(const.userSubmitterId, submissionSubmitterDto!!.submitter_id)
            Assertions.assertEquals(const.nextSubmissionId, submissionSubmitterDto.submission_id)

            //Assert no ApiSubmission insertions (meal does not have apiId, ingredients already inserted)
            Assertions.assertEquals(const.apiSubmissionDtos,
                    it.attach(ApiSubmissionDao::class.java).getAll()
            )


            Assertions.assertEquals(listOf(VOTABLE, REPORTABLE),
                    it.attach(SubmissionContractDao::class.java)
                            .getAllById(submissionDto.submission_id)
            )
        }
    }

    @Test
    fun shouldThrowOnWrongSubmitterId() {
        Assertions.assertThrows(InvalidInputException::class.java) {
            jdbi.inTransaction<Any, Exception> {
                throw InvalidInputException(InvalidInputDomain.VOTE, "")
            }
        }
    }


}