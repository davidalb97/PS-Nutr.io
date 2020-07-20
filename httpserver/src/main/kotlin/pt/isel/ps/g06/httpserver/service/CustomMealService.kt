package pt.isel.ps.g06.httpserver.service

import pt.isel.ps.g06.httpserver.dataAccess.common.responseMapper.CustomMealDbMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.CustomMealDbRepository
import pt.isel.ps.g06.httpserver.model.CustomMeal

class CustomMealService(
        private val customMealDbRepository: CustomMealDbRepository,
        private val customMealDbMapper: CustomMealDbMapper
) {

    fun getAllCustomMealsFromUser(submitterId: Int): Sequence<CustomMeal> =
            customMealDbRepository.getAllFromUser(submitterId).map { customMealDbMapper.mapToModel(it) }


    fun getCustomMealFromUser(submitterId: Int, mealName: String): CustomMeal =
            customMealDbMapper.mapToModel(customMealDbRepository.getCustomMealFromUser(submitterId, mealName))

    fun insertUserCustomMeal(
            submissionId: Int,
            mealName: String,
            mealPortion: Int,
            carbAmount: Int,
            imageUrl: String
    ): CustomMeal =
            customMealDbMapper.mapToModel(
                    customMealDbRepository.insertUserCustomMeal(
                            submissionId = submissionId,
                            mealName = mealName,
                            mealPortion = mealPortion,
                            carbAmount = carbAmount,
                            imageUrl = imageUrl
                    )
            )

    fun deleteUserCustomMeal(mealName: String): CustomMeal =
            customMealDbMapper.mapToModel(customMealDbRepository.deleteUserCustomMeal(mealName))
}
