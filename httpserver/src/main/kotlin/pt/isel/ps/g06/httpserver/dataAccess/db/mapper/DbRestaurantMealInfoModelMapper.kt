package pt.isel.ps.g06.httpserver.dataAccess.db.mapper

import org.springframework.stereotype.Component
import pt.isel.ps.g06.httpserver.dataAccess.common.mapper.ModelMapper
import pt.isel.ps.g06.httpserver.dataAccess.db.MEAL_TYPE_CUSTOM
import pt.isel.ps.g06.httpserver.dataAccess.db.SubmissionContractType
import pt.isel.ps.g06.httpserver.dataAccess.db.dto.DbRestaurantMealDto
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.*
import pt.isel.ps.g06.httpserver.model.MealRestaurantInfo
import pt.isel.ps.g06.httpserver.model.VoteState
import pt.isel.ps.g06.httpserver.model.Votes
import pt.isel.ps.g06.httpserver.model.modular.toUserPortion
import pt.isel.ps.g06.httpserver.model.modular.toUserPredicate
import pt.isel.ps.g06.httpserver.model.modular.toUserVote

@Component
class DbRestaurantMealInfoModelMapper(
        private val dbPortionsMapper: DbPortionModelMapper,
        private val dbVotesMapper: DbVotesModelMapper,
        private val dbMealRepo: MealDbRepository,
        private val dbPortionRepo: PortionDbRepository,
        private val dbFavoriteRepo: FavoriteDbRepository,
        private val dbReportRepo: ReportDbRepository,
        private val submissionDbRepository: SubmissionDbRepository,
        private val voteDbRepository: VoteDbRepository
) : ModelMapper<DbRestaurantMealDto, MealRestaurantInfo> {

    override fun mapTo(dto: DbRestaurantMealDto): MealRestaurantInfo {
        val isReportable = lazy {

            dto.submission_id?.let {
                //Restaurant meal requires contract
                submissionDbRepository.hasContract(it, SubmissionContractType.REPORTABLE)
                //A a future restaurant meal is only reportable if the type is custom
            } ?: (dbMealRepo.getById(dto.meal_submission_id)!!.meal_type == MEAL_TYPE_CUSTOM)
        }
        val isVotable = lazy {
            dto.submission_id?.let {
                //Restaurant meal requires contract
                submissionDbRepository.hasContract(it, SubmissionContractType.VOTABLE)
                //A a future restaurant meal is only votable if it's a suggested meal
            } ?: (dbMealRepo.getById(dto.meal_submission_id)!!.meal_type == MEAL_TYPE_CUSTOM)
        }
        return MealRestaurantInfo(
                identifier = dto.submission_id,
                restaurantIdentifier = dto.restaurant_submission_id,
                mealIdentifier = dto.meal_submission_id,
                isVerified = dto.verified,
                votes = lazy {
                    dto.submission_id?.let {
                        dbVotesMapper.mapTo(voteDbRepository.getVotes(it))
                    } ?: Votes(0, 0)
                },
                userVote = toUserVote { userId ->
                    dto.submission_id?.let { voteDbRepository.getUserVote(it, userId) } ?: VoteState.NOT_VOTED
                },
                isFavorable = toUserPredicate(default = { true }) { userId ->
                    //A user cannot favorite on it's own submission
                    dto.submission_id?.let { !submissionDbRepository.isSubmissionOwner(it, userId) }
                            ?: !submissionDbRepository.isSubmissionOwner(dto.meal_submission_id, userId)
                },
                isFavorite = toUserPredicate(default = { false }) { userId ->
                    dto.submission_id?.let { dbFavoriteRepo.getFavorite(it, userId) } ?: false
                },
                portions = dto.submission_id?.let(dbPortionRepo::getAllByRestaurantMealId)
                        ?.map(dbPortionsMapper::mapTo)
                        ?: emptySequence(),
                userPortion = toUserPortion { userId ->
                    dto.submission_id?.let {
                        dbPortionRepo.getUserPortion(it, userId)
                                ?.let(dbPortionsMapper::mapTo)
                    }
                },
                isReportable = toUserPredicate({ isReportable.value }) { userId ->
                    isReportable.value && dto.submission_id?.let {
                        //Cannot report on self submitted resource
                        !submissionDbRepository.isSubmissionOwner(it, userId)
                                //It is only reportable once per user
                                && !dbReportRepo.userHasReported(userId, it)
                    } ?: true
                },
                isVotable = toUserPredicate({ isVotable.value }) { userId ->
                    //A user cannot vote on it's own submission
                    isVotable.value && !submissionDbRepository.isSubmissionOwner(dto.meal_submission_id, userId)
                }
        )
    }
}