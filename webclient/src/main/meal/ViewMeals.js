import React, { useContext } from 'react'
import { LinkContainer } from 'react-router-bootstrap'
import { Link } from 'react-router-dom'


import Card from 'react-bootstrap/Card'
import Button from 'react-bootstrap/Button'

import RequestingEntity from '../common/RequestingEntity'

import MealList from './common/MealList'
import Loading from '../bootstrap-common/Loading'
export default function ViewMeals() {
    // const user = useContext(UserContext)
    // const [fetchState, response, json, error] = useFetch({ url: "", authToken: user.authToken })



    return <Card >
        <Card.Header as="h1">Your custom meals</Card.Header>
        <Card.Body>
            View all meals that were created by you.
            <p />
            Want to create a new one? Head over to<Link to="/meals/create"> meal creation.</Link>
            <hr />
            <RequestingEntity
                request={{ url: "/user/custom/meal" }}
                onSuccess={handleResult}
                onDefault={Loading}
            />
        </Card.Body>
    </Card>

    function handleResult({ json }) {
        if (!json.meals || json.meals.length <= 0) {
            return <LinkContainer to="/meals/create">
                <Button variant="info" block>You have no meals! Let's start by creating one</Button>
            </LinkContainer>
        }

        return <MealList meals={json.meals} />
    }
}