import React, { useState, useEffect, useContext } from 'react'
import useFetch, { FetchStates } from '../common/useFetch'
import RequestingEntity from '../common/RequestingEntity'

import UserContext from './UserContext'
import { get as getAuthToken, deleteCookie as deleteAuthToken } from './CookieService'

export default function UserProvider({ children }) {
    const defaultUser = useContext(UserContext)
    const [authToken, setAuthToken] = useState(getAuthToken())
    // const authToken = getAuthToken()
    console.log("user provider")

    //No need to trigger a request if no session cookie is present
    if (!authToken) return provideUserContext({ error: true })

    const request = { url: "http://localhost:9000/api/user", authToken: authToken }

    return <RequestingEntity
        request={request}
        onSuccess={provideUserContext}
        onInit={() => provideUserContext({})}
        onLoad={() => provideUserContext({})}
        onError={provideUserContext}
    />

    function provideUserContext({ json, error }) {
        let user = defaultUser

        if (json && !error) user = { authToken: authToken, user: json, initialized: true, onLogout: aux }
        if (error) user = { ...defaultUser, initialized: true, onLogin: setAuthToken }

        return <UserContext.Provider value={user}> {children} </UserContext.Provider>

        function aux() {
            console.log("Profile logout")
            deleteAuthToken()
            setAuthToken(undefined)
        }
    }
}