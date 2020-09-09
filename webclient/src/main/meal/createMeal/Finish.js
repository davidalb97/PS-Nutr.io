import React, { useReducer, useState, useEffect } from 'react'
import { LinkContainer } from 'react-router-bootstrap'

import Alert from 'react-bootstrap/Alert'
import Button from 'react-bootstrap/Button'
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'

import RequestingEntity from '../../common/RequestingEntity'

import Loading from '../../bootstrap-common/Loading'
import FetchError from '../../bootstrap-common/FetchError'
import SuccessAlert from '../../bootstrap-common/SuccessAlert'

export default function Finish({ meal, setCanAdvance }) {
    const [request] = useState({
        url: "/user/custom/meal",
        method: "POST",
        body: buildRequestBody(meal)
    })

    return <RequestingEntity
        request={request}
        onSuccess={sendSuccess}
        onError={FetchError}
        onDefault={Loading}
    />

    function buildRequestBody(meal) {
        return {
            name: meal.name,
            quantity: meal.quantity,
            unit: meal.unit,
            cuisines: meal.cuisines.map(cuisine => cuisine.value),
            ingredients: meal
                .ingredients
                .concat(meal.meals)
                .map(component => component.value)
                .map(component => {
                    return {
                        identifier: component.identifier,
                        quantity: component.userQuantity.amount
                    }
                })
        }
    }

    function sendSuccess() {
        return <SuccessAlert
            heading="Created meal with success!"
            body="You can now go back to the homepage or view created meals."
            buttons={[
                { link: "/meals", text: "View created meals" },
                { link: "/", text: "Homepage" },
            ]} />
    }
}