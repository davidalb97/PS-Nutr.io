package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import com.android.volley.VolleyError
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.data.model.UserSession

class MealInfoViewModel : ARecyclerViewModel<MealInfo>() {

    var mealInfo: MealInfo? = null
    var source: Source? = null
    var submissionId: Int? = null
    var restaurantId: String? = null
    var dbId: Long? = null

//    constructor(parcel: Parcel) : this(
//        mealInfo = parcel.readParcelable(MealInfo::class.java.classLoader),
//        source = Source.values()[parcel.readSerializable() as Int],
//        submissionId = parcel.readSerializable() as Int,
//        dbId = parcel.readSerializable() as Int
//    )

    fun setOnErrorFunc(onError: (Throwable) -> Unit) {
        this.onError = onError
    }

    override fun update() {
        if (mealInfo != null) {
            liveDataHandler.set(listOf(mealInfo!!))
        } else if (source != null) {
            //TODO logic should be in repo!
            if (source == Source.API || source == Source.FAVORITE) {
                if (restaurantId != null) {
                    mealRepository.getApiRestaurantMealInfo(
                        restaurantId = requireRestaurantId(),
                        mealId = requireSubmissionId(),
                        success = {
                            liveDataHandler.set(listOf(it.also {
                                it.source = source!!
                            }))
                        },
                        error = { throw it }
                    )
                } else {
                    mealRepository.getApiMealInfo(
                        mealId = submissionId!!,
                        success = {
                            liveDataHandler.set(listOf(it.also {
                                it.source = source!!
                            }))
                        },
                        error = { throw it }
                    )
                }
            } else {
                liveDataHandler.set(
                    mealRepository.getByIdAndSource(
                        dbId = dbId!!,
                        source = source!!
                    ), mealRepository.dbMealInfoMapper::mapToModel
                )
            }
        }
    }

    var restaurantInfo: RestaurantInfo? = null

    override fun writeToParcel(dest: Parcel?, flags: Int) {
//        dest?.writeParcelable(mealInfo, flags)
//        dest?.writeSerializable(source?.ordinal)
//        dest?.writeSerializable(submissionId)
//        dest?.writeSerializable(dbId)
        TODO("Save MealInfoViewModel to bundle")
    }

    override fun describeContents(): Int {
        return 0
    }

    fun setVote(
        vote: Boolean,
        success: () -> Unit,
        error: (VolleyError) -> Unit,
        userSession: UserSession
    ) {
        mealRepository.putVote(
            restaurantId = requireRestaurantId(),
            mealId = requireSubmissionId(),
            vote = vote,
            success = success,
            error = error,
            userSession = userSession
        )
    }

    fun requireSubmissionId(): Int {
        return when {
            mealInfo?.submissionId != null -> mealInfo?.submissionId!!
            submissionId != null -> submissionId!!
            else -> throw UnsupportedOperationException()
        }
    }

    fun requireRestaurantId(): String {
        return when {
            mealInfo?.restaurantSubmissionId != null -> mealInfo?.restaurantSubmissionId!!
            restaurantId != null -> restaurantId!!
            else -> throw UnsupportedOperationException()
        }
    }

    fun removeObservers(owner: LifecycleOwner) {
        liveDataHandler.removeObservers(owner)
    }

    companion object CREATOR : Parcelable.Creator<RestaurantRecyclerViewModel> {

        override fun createFromParcel(parcel: Parcel): RestaurantRecyclerViewModel =
            TODO("Restore RestaurantRecyclerViewModel from bundle")

        override fun newArray(size: Int): Array<RestaurantRecyclerViewModel?> {
            return arrayOfNulls(size)
        }

    }
}