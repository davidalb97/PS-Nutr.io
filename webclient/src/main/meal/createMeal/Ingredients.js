import React, { useState, useReducer, useEffect, useCallback } from 'react'

import Accordion from 'react-bootstrap/Accordion'
import Card from 'react-bootstrap/Card'
import ListGroup from 'react-bootstrap/ListGroup'

import RequestingEntity from '../../common/RequestingEntity'
export default function Ingredients({ setCanAdvance, setMeal }) {
    function reducer(list, food) {
        const newList = { ...list }
        newList[food.id] ? delete newList[food.id] : newList[food.id] = food
        return newList
    }

    const [foods, addFood] = useReducer(reducer, {})

    useEffect(() => {
        const canAdvance = Object.keys(foods).length > 0

        setCanAdvance(canAdvance)
        if (canAdvance) setMeal({ ingredients: Object.values(foods) })

    }, [foods, setCanAdvance, setMeal])


    return <>
        Choose meal composition:

        <FoodList
            request={{ url: "http://localhost:9000/api/ingredients" }}
            listName="Ingredients"
            foodMapper={(json) => json.ingredients}
            onFoodSelect={addFood}
        />
    </>
}

function FoodList({ request, listName, foodMapper, onFoodSelect }) {
    function listResult({ json }) {
        const foods = foodMapper(json).map((food, idx) => {
            return <Food
                key={idx}
                food={{ ...food, userQuantity: food.nutritionalInfo }}
                onFoodSelect={onFoodSelect}
            />
        })

        return <Accordion>
            <Card>
                <Accordion.Toggle as={Card.Header} eventKey="0">
                    {listName}
                </Accordion.Toggle>

                <Accordion.Collapse eventKey="0">
                    <ListGroup>
                        <div className="ingredient overflow" >
                            {foods}
                        </div>
                    </ListGroup>
                </Accordion.Collapse>
            </Card>
        </Accordion >
    }

    return <RequestingEntity
        request={request}
        onSuccess={listResult}
    />
}

//TODO When success and on click, background "green" doesn't go back to "light"
function Food({ food, onFoodSelect }) {
    const [isActive, setIsActive] = useState(false)

    function onClick() {
        setIsActive(!isActive)
        onFoodSelect(food)
    }

    return <ListGroup.Item onClick={onClick}>
        <Card bg={isActive ? "success" : "light"}>
            <Card.Title as="h1">{food.name}</Card.Title>
            <Card.Body>{food.id}</Card.Body>
        </Card>
    </ListGroup.Item>
}