package pt.isel.ps.g06.httpserver.springConfig.bean

import javax.crypto.spec.SecretKeySpec

class InsulinProfilesSecret(
        val key : SecretKeySpec,
        val transformation: String
)