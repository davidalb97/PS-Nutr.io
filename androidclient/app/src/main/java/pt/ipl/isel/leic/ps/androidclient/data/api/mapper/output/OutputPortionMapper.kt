package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.PortionOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.Portion

class OutputPortionMapper {

    fun mapToOutputModel(model: Portion) = PortionOutput(
        quantity = model.quantity,
        unit = model.unit.toString()
    )
}