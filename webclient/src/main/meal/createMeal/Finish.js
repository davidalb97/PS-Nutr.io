import React, { useReducer, useState, useEffect } from 'react'
import { Redirect } from 'react-router-dom'

import Alert from 'react-bootstrap/Alert'
import Button from 'react-bootstrap/Button'
import Spinner from 'react-bootstrap/Spinner'

import RequestingEntity from '../../common/RequestingEntity'
import Confirmation from './Confirmation'

export default function Finish({ meal, setCanAdvance }) {
    const [shouldRedirectHomepage, setshouldRedirectHomepage] = useState(false)
    const [request] = useState({
        url: "http://localhost:9000/api/meal",
        method: "POST",
        body: buildRequestBody(meal)
    })


    if (shouldRedirectHomepage) {
        return <Redirect to="/" />
    }

    return <>
        <RequestingEntity
            request={request}
            onSuccess={handleSuccess}
            onError={handleError}
            onLoad={handleLoading}
        />
        <Confirmation setCanAdvance={setCanAdvance} meal={meal} />
    </>


    function handleSuccess({ json }) {
        return <SuccessAlert onClose={() => setshouldRedirectHomepage(true)} />
    }

    function handleError({ error, json }) {
        return <ErrorAlert
            error={error}
            json={json}
            onClose={() => setshouldRedirectHomepage(true)}
        />
    }

    function handleLoading() {
        return <Spinner animation="border" role="status">
            <span className="sr-only">Loading...</span>
        </Spinner>
    }

    function buildRequestBody(meal) {
        return {
            name: meal.name,
            quantity: meal.quantity,
            unit: meal.unit,
            ingredients: meal.ingredients.map(ingredient => {
                return {
                    identifier: ingredient.id,
                    quantity: ingredient.userQuantity.amount
                }
            }),
            cuisines: ["Portuguese"]
        }
    }
}


function ErrorAlert({ error, json, onClose }) {
    const [show, setShow] = useState(true)

    return <Alert show={show} variant="danger">
        <Alert.Heading>Failed to send custom meal!</Alert.Heading>
        <p>
            You should go back to the homepage and try again later.
        </p>
        <hr />
        <div className="d-flex justify-content-end">
            <Button onClick={() => { setShow(false); onClose() }} variant="outline-danger">
                Homepage
          </Button>
        </div>
    </Alert >
}

function SuccessAlert({ onClose }) {
    const [show, setShow] = useState(true)

    return <Alert show={show} variant="success">
        <Alert.Heading>Created meal with success!</Alert.Heading>
        <p>
            You can now go back to the homepage.
        </p>
        <hr />
        <div className="d-flex justify-content-end">
            <Button onClick={() => { setShow(false); onClose() }} variant="outline-sucess">
                Homepage
          </Button>
        </div>
    </Alert >
}