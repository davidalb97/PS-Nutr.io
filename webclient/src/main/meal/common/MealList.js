import React, { useState, useReducer } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'

import ListGroup from 'react-bootstrap/ListGroup'
import Card from 'react-bootstrap/Card'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Tabs from 'react-bootstrap/Tabs'
import Tab from 'react-bootstrap/Tab'
import Nav from 'react-bootstrap/Nav'
import Button from 'react-bootstrap/Button'


const meals = Array
    .from(Array(100).keys())
    .map(aux => { return { name: aux, mealIdentifier: aux } })

export default function MealList() {
    const [activeMeals, onMealDelete] = useReducer(removeFromArray, meals)
    const [request, triggerRequest] = useReducer(reducer, {})

    return <Tab.Container defaultActiveKey="0" mountOnEnter onSelect={triggerRequest}>
        <Row>
            <Col sm={3} className="overflow nav-tab">
                {/* Side buttons */}
                <Nav variant="pills" className="flex-column" justify>
                    {activeMeals.map((meal, idx) => {
                        return <Nav.Item key={idx}><Nav.Link eventKey={idx}>{meal.name}</Nav.Link> </Nav.Item>
                    })}
                </Nav>
            </Col>
            <Col sm={9} >
                {/* Actual meal content */}
                <Tab.Content >
                    {activeMeals.map((meal, idx) => {
                        return <Tab.Pane key={idx} eventKey={idx}>
                            <Meal onMealDelete={onMealDelete} />
                        </Tab.Pane>
                    })}
                </Tab.Content>
            </Col>
        </Row>
    </Tab.Container>

    function reducer(request, mealId) {
        return request
    }

    function removeFromArray(list, id) {
        return [...list].filter(meal => meal.mealIdentifier !== id)
    }
}

function Meal({ onMealDelete }) {
    const meal = { mealIdentifier: 1, name: "Fruit Salad", nutritionalInfo: { amount: 200, carbs: 100, unit: "gr" } }

    const composedByIngredients = [
        { name: "Apple", nutritionalInfo: { amount: 100, carbs: 20, unit: "gr" } },
        { name: "Banana", nutritionalInfo: { amount: 50, carbs: 10, unit: "gr" } }
    ]

    const composedByMeals = []
    const nutritionalInfo = meal.nutritionalInfo

    return <>
        <Card.Header as="h2">{meal.name}</Card.Header>
        <Card.Body>
            <p><strong>Quantity:</strong> {nutritionalInfo.amount} {nutritionalInfo.unit}</p>
            <p><strong>Carbohydrates:</strong> {nutritionalInfo.carbs} gr</p>


            <Tabs defaultActiveKey="meals">
                <Tab eventKey="meals" title="Meals">
                    <Card.Body>
                        <ListGroup variant="flush">
                            {composedByMeals.map((composition, idx) => <MealComposition key={idx} food={composition} />)}
                        </ListGroup>
                    </Card.Body>
                </Tab>
                <Tab eventKey="ingredients" title="Ingredients">
                    <Card.Body>
                        <ListGroup variant="flush">
                            {composedByIngredients.map((composition, idx) => <MealComposition key={idx} food={composition} />)}
                        </ListGroup>
                    </Card.Body>
                </Tab>
            </Tabs>
            <Button variant="danger" block onClick={deleteMeal}>Delete</Button>
        </Card.Body>
    </>

    function deleteMeal() {
        onMealDelete(meal.mealIdentifier)
    }
}

function MealComposition({ food }) {
    const nutrition = food.nutritionalInfo

    return <ListGroup.Item>
        {nutrition.amount}{nutrition.unit} of <strong>{food.name}</strong> with {} gr of carbohydrates.
    </ListGroup.Item>
}