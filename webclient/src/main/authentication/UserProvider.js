import React, { useState, useEffect, useContext } from 'react'
import useFetch, { FetchStates } from '../common/useFetch'
import RequestingEntity from '../common/RequestingEntity'

import UserContext from './UserContext'
import { get as getAuthToken } from './CookieService'

export default function UserProvider({ children }) {
    const defaultUser = useContext(UserContext)
    const authToken = getAuthToken()

    //No need to trigger a request if no session cookie is present
    const request = authToken ? { url: "http://localhost:9000/api/user/info", authToken: authToken } : {}

    return <RequestingEntity
        request={request}
        onSuccess={provideUserContext}
        onInit={() => provideUserContext({})}
        onLoad={() => provideUserContext({})}
        onError={provideUserContext}
    />

    function provideUserContext({ json, error }) {
        let user = defaultUser

        if (json && !error) user = { authToken: authToken, user: json, initialized: true }
        if (error) user = { ...defaultUser, initialized: true }
       
        return <UserContext.Provider value={user}> {children} </UserContext.Provider>
    }
}