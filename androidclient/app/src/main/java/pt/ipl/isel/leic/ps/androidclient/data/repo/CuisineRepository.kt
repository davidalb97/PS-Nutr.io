package pt.ipl.isel.leic.ps.androidclient.data.repo

import pt.ipl.isel.leic.ps.androidclient.data.api.datasource.CuisineDataSource
import pt.ipl.isel.leic.ps.androidclient.data.api.mapper.input.InputCuisineMapper
import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine

class CuisineRepository(private val dataSource: CuisineDataSource) {

    val inputCuisineMapper = InputCuisineMapper()

    fun getCuisines(
        count: Int,
        skip: Int,
        success: (List<Cuisine>) -> Unit,
        error: (Throwable) -> Unit
    ) {
        dataSource.getCuisines(
            count = count,
            skip = skip,
            success = { success(inputCuisineMapper.mapToListModel(it)) },
            error = error
        )
    }
}