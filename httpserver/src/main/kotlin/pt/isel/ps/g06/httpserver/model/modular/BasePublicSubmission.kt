package pt.isel.ps.g06.httpserver.model.modular

import java.net.URI

open class BasePublicSubmission<Id>(
        identifier: Id,
        override val image: URI?,
        override val name: String
) : BaseSubmission<Id>(
        identifier = identifier
), INameable, IImage