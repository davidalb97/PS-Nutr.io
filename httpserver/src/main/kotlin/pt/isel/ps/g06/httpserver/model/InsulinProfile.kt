package pt.isel.ps.g06.httpserver.model

import pt.isel.ps.g06.httpserver.security.converter.AColumnCryptoConverter
import pt.isel.ps.g06.httpserver.security.converter.IntColumnCryptoConverter
import pt.isel.ps.g06.httpserver.security.converter.OffsetDateTimeColumnCryptoConverter
import pt.isel.ps.g06.httpserver.security.converter.StringColumnCryptoConverter
import java.io.Serializable
import java.time.OffsetDateTime
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class InsulinProfile(
        @Id var submitterId: Int = 0,

        @Convert(converter = StringColumnCryptoConverter::class)
        @Id var profileName: String = "",

        @Convert(converter = StringColumnCryptoConverter::class)
        var startTime: String = "",

        @Convert(converter = StringColumnCryptoConverter::class)
        var endTime: String = "",

        @Convert(converter = IntColumnCryptoConverter::class)
        var glucoseObjective: Int = 0,

        @Convert(converter = IntColumnCryptoConverter::class)
        var insulinSensitivityFactor: Int = 0,

        @Convert(converter = IntColumnCryptoConverter::class)
        var carbohydrateRatio: Int = 0,

        @Convert(converter = OffsetDateTimeColumnCryptoConverter::class)
        var modificationDate: OffsetDateTime = OffsetDateTime.now()
) : Serializable