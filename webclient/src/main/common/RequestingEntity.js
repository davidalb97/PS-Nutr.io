import React, { useContext } from 'react'
import useFetch, { FetchStates } from './useFetch'

import UserContext from '../authentication/UserContext'

/**
 * Generic component responsible for sending an HTTP request and acting on it based on its' response.
 * 
 * @param {Object} request - Necessary object to send an HTTP request with following fields:
 *  - url (mandatory)
 *  - authToken (optional, defaults to UserContext token)
 *  - contentType (optional, defaults to 'application/json')
 *  - method (optional, defaults to 'GET')
 *  - body (optional)
 */
export default function RequestingEntity({ request, onLoad, onSuccess, onError, onInit }) {
    if (!request.authToken) {
        const user = useContext(UserContext)
        request.authToken = user.authToken
    }

    const [fetchState, response, json, error] = useFetch(request)

    switch (fetchState) {
        case FetchStates.init: return onInit ? onInit() : null
        case FetchStates.fetching: return onLoad ? onLoad() : null
        case FetchStates.error: return onError ? onError({ error: error, json: json }) : null
        case FetchStates.done: return onSuccess ? onSuccess({ response: response, json: json }) : null
        default: return null
    }
}