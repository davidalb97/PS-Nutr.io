package pt.isel.ps.g06.httpserver.model.modular

import java.net.URI

open class BasePublicSubmission<Id>(
        identifier: Id,
        val image: URI?,
        override val name: String,
        override val isFavorable: UserPredicate,
        override val isFavorite: UserPredicate
) : BaseSubmission<Id>(
        identifier = identifier
), INameable, IFavorable