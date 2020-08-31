package pt.isel.ps.g06.httpserver.dataAccess.output.modular

import pt.isel.ps.g06.httpserver.model.modular.BaseSubmission
import java.net.URI

open class BasePublicSubmissionOutput<Id>(
        identifier: Id,
        val image: URI?,
        override val name: String,
        override val favorites: FavoritesOutput
) : BaseSubmission<Id>(
        identifier = identifier
), INameableOutput, IFavorableOutput