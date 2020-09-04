import React, { useState, useReducer, useEffect } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'

import ListGroup from 'react-bootstrap/ListGroup'
import Card from 'react-bootstrap/Card'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Tabs from 'react-bootstrap/Tabs'
import Tab from 'react-bootstrap/Tab'
import Nav from 'react-bootstrap/Nav'
import Button from 'react-bootstrap/Button'

import Loading from '../../bootstrap-common/Loading'

export default function MealList({ meals }) {
    const [activeMeals, setActiveMeals] = useState(meals)

    return <Tab.Container defaultActiveKey="0" mountOnEnter>
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
                            <Meal
                                onMealDelete={removeFromList.bind(meal.mealIdentifier)}
                                identifier={meal.mealIdentifier}
                            />
                        </Tab.Pane>
                    })}
                </Tab.Content>
            </Col>
        </Row>
    </Tab.Container>

    function removeFromList() {
        //Where 'this' is meal identifier defined by bind() call
        const newList = [...activeMeals].filter(meal => meal.mealIdentifier !== this)
        setActiveMeals(newList)
    }
}

function Meal({ onMealDelete, identifier }) {
    const [request, setRequest] = useState({})
    const [onDelete, triggerDeletion] = useReducer(_ => true, false);
    const [fetchState, response, json, error] = useFetch(request)
    const [meal, setMealDetail] = useState()

    useEffect(() => {
        if (!meal) setRequest({ url: `http://localhost:8080/meal/${identifier}` })

        //Clear request so that meal is cached and not obtained multiple times on next renders (on menu iterating)
        else setRequest({})
    }, [setRequest])

    useEffect(() => {
        if (fetchState === FetchStates.done && json && !meal) setMealDetail(json)
        if (fetchState === FetchStates.done && onDelete) onMealDelete()
    }, [fetchState, setMealDetail])


    if (error) return <FetchError error={error} json={json} />
    if (!meal || fetchState === FetchStates.fetching) return <Loading />

    const meals = meal
        .composedBy
        .meals
        .map((component, idx) => <MealComposition key={idx} component={component} />)

    const ingredients = meal
        .composedBy
        .ingredients
        .map((component, idx) => <MealComposition key={idx} component={component} />)

    return <>
        <Card.Header as="h2">{meal.name}</Card.Header>
        <Card.Body>
            <p><strong>Quantity:</strong> {meal.nutritionalInfo.amount} {meal.nutritionalInfo.unit}</p>
            <p><strong>Carbohydrates:</strong> {meal.nutritionalInfo.carbs}{meal.nutritionalInfo.unit}</p>

            <Tabs defaultActiveKey="meals">
                <Tab eventKey="meals" title="Meals">
                    <Card.Body>
                        <ListGroup variant="flush">
                            {meals.length ? meals : "This meal is not composed by other meals."}
                        </ListGroup>
                    </Card.Body>
                </Tab>
                <Tab eventKey="ingredients" title="Ingredients">
                    <Card.Body>
                        <ListGroup variant="flush">
                            {ingredients.length ? ingredients : "This meal is not composed by any ingredients."}
                        </ListGroup>
                    </Card.Body>
                </Tab>
            </Tabs>
            <div className="spacer-20" />
            <Button variant="danger" block onClick={deleteMeal}>Delete</Button>
        </Card.Body>
    </>

    function deleteMeal() {
        setRequest({ url: `http://localhost:8080/meal/${identifier}`, method: "DELETE" })
        triggerDeletion()
    }
}

function MealComposition({ component }) {
    const nutrition = component.nutritionalInfo

    return <ListGroup.Item>
        {nutrition.amount}{nutrition.unit} of <strong>{component.name}</strong> with {nutrition.carbs}gr of carbohydrates.
    </ListGroup.Item>
}