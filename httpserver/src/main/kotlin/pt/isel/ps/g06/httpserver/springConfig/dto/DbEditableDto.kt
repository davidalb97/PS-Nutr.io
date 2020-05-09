package pt.isel.ps.g06.httpserver.springConfig.dto

import org.springframework.boot.convert.DurationUnit
import org.springframework.context.annotation.Configuration
import java.time.Duration
import java.time.temporal.ChronoUnit
import javax.validation.constraints.Max
import javax.validation.constraints.Min

class DbEditableDto {
    @Min(0)
    @Max(30)
    @DurationUnit(ChronoUnit.MINUTES)
    var `edit-timeout-minutes`: Duration? = null
}