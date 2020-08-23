import React, { useState, useEffect, useContext } from 'react'
import useFetch, { FetchStates } from '../common/useFetch'

import UserContext from './UserContext'
import { get as getAuthToken } from './CookieService'

export default function UserProvider({ onLogin, children }) {
    const [request, setRequest] = useState({})
    const [fetchState, response, json, error] = useFetch(request)
    const context = useContext(UserContext)
    const authToken = getAuthToken()

    let user = { ...context }

    if (authToken) setRequest({ url: "http://localhost:9000/api/user", authToken: context.authToken })
    if (fetchState === FetchStates.done) user = { authToken: authToken, user: json }

    return <UserContext.Provider value={user}>
        {children}
    </UserContext.Provider>
}