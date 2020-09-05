import React, { useState, useEffect, useContext } from 'react'
import useFetch, { FetchStates } from '../common/useFetch'
import RequestingEntity from '../common/RequestingEntity'

import UserContext from './UserContext'
import { get as getAuthToken, deleteCookie as deleteAuthToken } from './CookieService'

export default function UserProvider({ children }) {
    const unitializedUser = useContext(UserContext)
    const [authToken, setAuthToken] = useState(getAuthToken())

    //No need to trigger a request if no session cookie is present
    if (!authToken) return provideUserContext({ error: true })

    const request = { url: "http://localhost:8080/user", authToken: authToken }
    // console.log(request)

    return <RequestingEntity
        request={request}
        onSuccess={provideUserContext}
        onDefault={provideUserContext}
        onError={() => { deleteAuthToken(); provideUserContext({ error: true }) }}
    />

    function provideUserContext({ error, json }) {
        let user = unitializedUser

        // console.log(error)
        // console.log(json)

        if (json && !error) user = { authToken: authToken, user: json, initialized: true, onLogout: logout }
        if (error) user = { ...unitializedUser, initialized: true, onLogin: setAuthToken }

        // console.log(user)
        return <UserContext.Provider value={user}> {children} </UserContext.Provider>

        function logout() {
            deleteAuthToken()
            setAuthToken(undefined)
        }
    }
}