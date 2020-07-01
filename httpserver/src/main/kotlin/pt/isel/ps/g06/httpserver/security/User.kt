package pt.isel.ps.g06.httpserver.security

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
public class User(
        @Id val submitterId: Long,
        val username: String,
        val password: String
) {
    constructor() : this(-1, "", "") {
    }
}