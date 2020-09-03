package pt.isel.ps.g06.httpserver.dataAccess.output.modular

import java.net.URI

open class BasePublicSubmissionOutput<Id>(
        identifier: Id,
        val image: URI?,
        override val name: String,
        override val favorites: FavoritesOutput
) : BaseSubmissionOutput<Id>(
        identifier = identifier
), INameableOutput, IFavorableOutput