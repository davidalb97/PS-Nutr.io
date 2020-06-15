package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import pt.ipl.isel.leic.ps.androidclient.NutrioApp.Companion.mealRepository
import pt.ipl.isel.leic.ps.androidclient.data.model.MealInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.RestaurantInfo
import pt.ipl.isel.leic.ps.androidclient.data.model.Source
import pt.ipl.isel.leic.ps.androidclient.ui.util.LiveDataHandler

class MealInfoViewModel : ARecyclerViewModel<MealInfo>() {

    var mealInfo: MealInfo? = null
    var source: Source? = null
    var submissionId: Int? = null
    var dbId: Long? = null

//    constructor(parcel: Parcel) : this(
//        mealInfo = parcel.readParcelable(MealInfo::class.java.classLoader),
//        source = Source.values()[parcel.readSerializable() as Int],
//        submissionId = parcel.readSerializable() as Int,
//        dbId = parcel.readSerializable() as Int
//    )

    override fun update() {
        if (mealInfo != null) {
            liveDataHandler.set(listOf(mealInfo!!))
        } else if (source != null) {
            //TODO logic should be in repo!
            if (source == Source.API) {
                mealRepository.getApiMealInfo(
                    mealId = submissionId!!,
                    success = { liveDataHandler.set(listOf(it)) },
                    error = onError
                )
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
    }

    fun hasMeal(): Boolean {
        return mealInfo != null || source != null
    }


    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantRecyclerViewModel> {

        override fun createFromParcel(parcel: Parcel): RestaurantRecyclerViewModel =
            TODO()

        override fun newArray(size: Int): Array<RestaurantRecyclerViewModel?> {
            return arrayOfNulls(size)
        }

    }
}