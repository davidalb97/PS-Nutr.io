import React, { useContext } from 'react'
import useFetch, { FetchStates } from './useFetch'

import UserContext from '../authentication/UserContext'

/**
 * Generic component responsible for sending an HTTP request and acting on it based on its' response.
 * 
 * @param {Object} request - Necessary object to send an HTTP request with following fields:
 *  - url (mandatory)
 *  - contentType (optional, defaults to 'application/json')
 *  - method (optional, defaults to 'GET')
 *  - body (optional)
 */
export default function RequestingEntity({ request, onLoad, onSuccess, onError, onInit, onDefault = () => <> </> }) {
    const user = useContext(UserContext)
    request.authToken = request.authToken || user.authToken

    const [fetchState, response, json, error] = useFetch(request)

    switch (fetchState) {
        case FetchStates.init: return onInit ? onInit() : onDefault()
        case FetchStates.fetching: return onLoad ? onLoad() : onDefault()
        case FetchStates.error: return onError ? onError({ error: error, json: json }) : onDefault()
        case FetchStates.done: return onSuccess ? onSuccess({ response: response, json: json }) : onDefault()
        default: return onDefault()
    }
}