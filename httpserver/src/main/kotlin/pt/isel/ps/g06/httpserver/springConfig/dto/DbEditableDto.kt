package pt.isel.ps.g06.httpserver.springConfig.dto

import org.springframework.boot.convert.DurationUnit
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.validation.constraints.Max
import javax.validation.constraints.Min

//TODO Delete this
class DbEditableDto {
    @Min(0)
    @Max(30)
    @DurationUnit(ChronoUnit.MINUTES)
    var `edit-timeout-minutes`: Duration? = null
}