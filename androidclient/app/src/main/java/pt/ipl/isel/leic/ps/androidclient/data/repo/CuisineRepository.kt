package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.data.source.DataSource
import pt.ipl.isel.leic.ps.androidclient.data.source.model.Cuisine

class CuisineRepository(private val dataSource: DataSource) {

    fun getCuisines(
        success: (List<Cuisine>) -> Unit,
        error: () -> Unit,
        uriParameters: HashMap<String, HashMap<String, String>>,
        count: Int,
        skip: Int
    ) {
        dataSource.getCuisines(
            { success(it) },
            { error() },
            uriParameters,
            count,
            skip
        )
    }
}