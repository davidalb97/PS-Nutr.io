package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.PortionOutput
import pt.ipl.isel.leic.ps.androidclient.data.model.Portion
import pt.ipl.isel.leic.ps.androidclient.ui.util.units.DEFAULT_WEIGHT_UNIT

class OutputPortionMapper {

    fun mapToOutputModel(model: Portion): PortionOutput {
        return PortionOutput(
            //Server compatibility conversion
            quantity = model.unit.convert(DEFAULT_WEIGHT_UNIT, model.quantity),
            unit = DEFAULT_WEIGHT_UNIT.toString()
        )
    }
}