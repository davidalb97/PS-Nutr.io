import { useEffect, useState } from 'react'
export const FetchStates = {
    init: 'init',
    fetching: 'fetching',
    error: 'error',
    done: 'done'
}

const acceptTypes = ['application/json', 'application/problem+json']

export default function useFetch({
    url,
    authToken,
    contentType,
    method,
    body
}) {
    const [fetchState, setFetchState] = useState()
    const [response, setResponse] = useState()
    const [json, setJson] = useState()
    const [error, setError] = useState()

    useEffect(() => {
        let isCancelled = false

        async function get() {
            if (!url || url === '') {
                setFetchState(FetchStates.init)
                return
            }

            let initFetch = setupRequest({
                url,
                authToken,
                contentType,
                method,
                body
            })

            try {
                setFetchState(FetchStates.fetching)

                const resp = await fetch(initFetch.url, initFetch.requestInit)

                if (isCancelled) {
                    logResponse({ method: initFetch.requestInit.method, url: initFetch.url, isCancelled: true, response: resp })
                    return
                }

                const contentType = resp.headers.get('Content-Type')
                const json = isJson(contentType) ? await resp.json() : null

                if (isCancelled) {
                    logResponse({ method: initFetch.requestInit.method, url: initFetch.url, isCancelled: true, response: resp })
                    return
                }

                setResponse(resp)
                if (json) setJson(json)
                setFetchState(resp.ok ? FetchStates.done : FetchStates.error)

            } catch (e) {
                if (!isCancelled) {
                    setFetchState(FetchStates.error)
                    setError(e)
                }
                logResponse({ method: initFetch.requestInit.method, url: initFetch.url, error: e.message })
            }
        }

        get()
        return () => { isCancelled = true }
    }, [url, authToken, contentType, method, body, setFetchState, setResponse, setJson, setError])

    return [fetchState, response, json, error]
}

function setupRequest({
    url,
    authToken,
    contentType,
    method,
    body
}) {

    let result = {
        url: url,
        requestInit: {
            headers: {
                'Content-Type': contentType || 'application/json',
                'Accept': acceptTypes
            },
            method: method || 'GET'
        }
    }

    //Add auth token to authorization header if exists
    if (authToken) {
        result.requestInit.headers['Authorization'] = `Bearer ${authToken}`
    }

    //Add body if exists
    if (body) {
        result.requestInit.body = JSON.stringify(body)
    }

    console.log(`>> Outgoing HTTP request: '${result.requestInit.method} ${result.url}'`)
    return result
}

function isJson(rspContentType) {
    return rspContentType && isValidContentType(rspContentType)
}

function isValidContentType(contentType) {
    return acceptTypes.some(accept => contentType === accept)
}

function logResponse({ method, url, isCancelled, response, error, json }) {
    console.table({
        request: `${method} ${url}`,
        isCancelled: isCancelled,
        response: response,
        error: error,
        json: json ? JSON.stringify(json) : null
    })
}
