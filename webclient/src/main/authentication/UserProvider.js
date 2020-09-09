import React, { useState, useContext } from 'react'
import RequestingEntity from '../common/RequestingEntity'

import UserContext from './UserContext'
import { get as getAuthToken, deleteCookie as deleteAuthToken } from './CookieService'

export default function UserProvider({ children }) {
    const defaultUser = useContext(UserContext)
    const [authToken, setAuthToken] = useState(getAuthToken())

    //No need to trigger a request if no session cookie is present
    if (!authToken) return provideUserContext({ error: true })

    const request = { url: "/user", authToken: authToken }

    return <RequestingEntity
        request={request}
        onSuccess={provideUserContext}
        onInit={provideUserContext}
        onLoad={provideUserContext}
        onError={() => { deleteAuthToken(); return provideUserContext({ error: true }) }}
    />


    function provideUserContext({ json, error }) {
        let user = defaultUser

        if (json && !error) user = { authToken: authToken, user: json, initialized: true, onLogout: logout }
        if (error) user = { ...defaultUser, initialized: true, onLogin: setAuthToken }

        return <UserContext.Provider value={user}> {children} </UserContext.Provider>

        function logout() {
            deleteAuthToken()
            setAuthToken(undefined)
        }
    }
}