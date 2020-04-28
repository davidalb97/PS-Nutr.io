package pt.ipl.isel.leic.ps.androidclient.ui.viewmodel

import androidx.lifecycle.LiveData
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Restaurant

class RestaurantViewModel : AViewModel<Restaurant>() {

    override fun getItems() {
        TODO("Not yet implemented")
    }

    override fun getMoreItems() {
        TODO("Not yet implemented")
    }

    override fun deleteItem(item: Restaurant) {
        TODO("Not yet implemented")
    }

    override fun fetchLiveData(): LiveData<List<Restaurant>>? {
        TODO("Not yet implemented")
    }
}
