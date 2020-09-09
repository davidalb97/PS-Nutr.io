import React, { useContext } from 'react'
import { Link } from 'react-router-dom'

import Card from 'react-bootstrap/Card'
import UserContext from './UserContext'

class Credentials {
    constructor(predicate, body) {
        this.predicate = predicate
        this.body = body
    }
}
export const RequiredCredentials = {
    loggedIn: new Credentials(
        user => user != undefined,
        <span>
            The page you are trying to access requires you to be logged in.
            <p />
            Either <Link to="/login">log in </Link> or <Link to="/register">register</Link> if you don't have an account.
        </span>
    ),

    moderator: new Credentials(
        user => user && user.userRole == 'mod',
        <span>
            The page you are trying to access requires you to be a moderator!
            <p />
            If you think this is an error, please contact us.
        </span>
    )
}
export const loggedIn = RequiredCredentials.loggedIn
export const moderator = RequiredCredentials.moderator

export default function RequireCredentials({ children, requiredCredentials }) {
    const userContext = useContext(UserContext)

    if (!requiredCredentials.predicate(userContext.user)) {
        return <Card>
            <Card.Header as="h1">Credentials required.</Card.Header>
            <Card.Body>
                {requiredCredentials.body}
            </Card.Body>
        </Card>
    }

    return <> {children} </>
}