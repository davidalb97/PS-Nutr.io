import React, { useContext } from 'react'
import { Link } from 'react-router-dom'

import Card from 'react-bootstrap/Card'
import ListGroup from 'react-bootstrap/ListGroup'

import RequestingEntity from '../common/RequestingEntity'

import MealList from './common/MealList'

export default function ViewMeals() {
    // const user = useContext(UserContext)
    // const [fetchState, response, json, error] = useFetch({ url: "", authToken: user.authToken })



    return <Card >
        <Card.Header as="h1">Your custom meals</Card.Header>
        <Card.Body>
            View all meals that were created by you.
            Want to create a new one? Head over to<Link to="/meals/create"> meal creation.</Link></Card.Body>
        <MealList />
        {/* <RequestingEntity
            request={request}
            onSuccess={MealList}
            onLoad={handleLoad}
        /> */}
    </Card>

    function displayLoading() {

    }

    function displayError() {

    }
}