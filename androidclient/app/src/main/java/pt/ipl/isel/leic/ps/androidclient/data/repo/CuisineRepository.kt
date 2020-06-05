package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.CuisineDataSource
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine

class CuisineRepository(private val dataSource: CuisineDataSource) {

    fun getCuisines(
        success: (List<Cuisine>) -> Unit,
        error: (Throwable) -> Unit,
        uriParameters: HashMap<String, String>?,
        count: Int,
        skip: Int
    ) {
        /*dataSource.getCuisines(
            { success(it) },
            { error() },
            uriParameters,
            count,
            skip
        )*/
    }
}