import React, { useContext } from 'react'
import useFetch, { FetchStates } from './useFetch'

import UserContext from '../authentication/UserContext'
import FetchError from '../bootstrap-common/FetchError'

/**
 * Generic component responsible for sending an HTTP request and acting on it based on its' response.
 * 
 * @param {Object} request - Necessary object to send an HTTP request with following fields:
 *  - url (mandatory)
 *  - contentType (optional, defaults to 'application/json')
 *  - method (optional, defaults to 'GET')
 *  - body (optional)
 */
export default function RequestingEntity({
    request,
    onLoad: Load,
    onSuccess: Success,
    onError: Error,
    onInit: Init,
    onDefault: Default = () => <> </>
}) {
    const user = useContext(UserContext)
    request.authToken = request.authToken || user.authToken

    const [fetchState, response, json, error] = useFetch(request)

    switch (fetchState) {
        case FetchStates.init: return Init ? <Init /> : <Default />
        case FetchStates.fetching: return Load ? <Load /> : <Default />
        case FetchStates.error: return Error ? <Error error={error} json={json} /> : <FetchError error={error} json={json} />
        case FetchStates.done: return Success ? <Success response={response} json={json} /> : <Default />
        default: return <Default />
    }
}