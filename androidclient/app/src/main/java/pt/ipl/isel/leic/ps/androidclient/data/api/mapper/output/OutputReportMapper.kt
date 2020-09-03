package pt.ipl.isel.leic.ps.androidclient.data.api.mapper.output

import pt.ipl.isel.leic.ps.androidclient.data.api.dto.output.ReportOutput

class OutputReportMapper {

    fun mapToOutputModel(reportMsg: String) = ReportOutput(
        description = reportMsg
    )
}