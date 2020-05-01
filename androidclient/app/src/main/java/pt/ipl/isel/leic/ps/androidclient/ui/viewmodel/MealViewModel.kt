package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Meal

class MealViewModel : AViewModel<Meal>() {

    override fun fetchLiveData(): LiveData<List<Meal>>? {
        TODO("Not yet implemented")
    }
}