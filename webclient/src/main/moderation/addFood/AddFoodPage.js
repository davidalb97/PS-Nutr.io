import React, { useEffect, useState } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'

import Spinner from 'react-bootstrap/Spinner'

import AddFoodForm from './AddFoodForm'
import ServerOffline from '../../errors/ServerOffline'
export default function AddFoodPage() {
    const [request] = useState({ url: "http://localhost:9000/api/cuisine" })
    const [cuisines, setCuisines] = useState(undefined)
    const [fetchState, response, json, error] = useFetch(request)

    //TODO Check if user is allowed to view resource

    //Update cuisines on valid response
    useEffect(() => {
        if (json && fetchState === FetchStates.done) {
            setCuisines(json.cuisines)
        }
    }, [fetchState])

    if (fetchState === FetchStates.error) return <ServerOffline />

    return cuisines ? <AddFoodForm cuisines={cuisines} /> :
        <Spinner animation="border" role="status">
            <span className="sr-only">Loading...</span>
        </Spinner>
}