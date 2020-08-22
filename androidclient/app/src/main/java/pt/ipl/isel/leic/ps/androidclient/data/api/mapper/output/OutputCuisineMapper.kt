package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.model.Cuisine


class OutputCuisineMapper {

    fun mapToOutputModel(model: Cuisine) = model.name

    fun mapToOutputModelCollection(models: Collection<Cuisine>) =
        models.map(this::mapToOutputModel)
}