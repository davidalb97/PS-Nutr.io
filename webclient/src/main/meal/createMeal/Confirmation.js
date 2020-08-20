import React, { useEffect } from 'react'

import Card from 'react-bootstrap/Card'
import Accordion from 'react-bootstrap/Accordion'
import ListGroup from 'react-bootstrap/ListGroup'

const meal = {
    name: "Salad",
    quantity: 100,
    ingredients: [
        {
            "id": 171,
            "name": "Apple",
            "nutritionalInfo": {
                "carbs": 10,
                "amount": 85,
                "unit": "gr"
            },
            "userQuantity": {
                "carbs": 10,
                "amount": 85,
                "unit": "gr"
            }
        },
        {
            "id": 165,
            "name": "Banana, with peel",
            "nutritionalInfo": {
                "carbs": 20,
                "amount": 130,
                "unit": "gr"
            },
            "userQuantity": {
                "carbs": 20,
                "amount": 130,
                "unit": "gr"
            }
        },
        {
            "id": 164,
            "name": "Banana, without peel",
            "nutritionalInfo": {
                "carbs": 20,
                "amount": 85,
                "unit": "gr"
            },
            "userQuantity": {
                "carbs": 20,
                "amount": 85,
                "unit": "gr"
            }
        }
    ]
}

export default function Confirmation({ setCanAdvance }) {
    useEffect(() => { setCanAdvance(true) }, [])

    const totalCarbs = meal.ingredients.reduce((total, ingredient) => {
        return + ingredient.userQuantity.carbs + total
    }, 0)

    return <Card>
        <Card.Header as="h1">Confirm your meal:</Card.Header>
        <Card.Body>
            <Card.Text>
                <p><strong>Name:</strong> {meal.name} </p >
                <p><strong>Quantity:</strong> {meal.quantity} grams </p>
                <p><strong>Total carbs:</strong> {totalCarbs} grams</p >
                <p><strong>Cuisines:</strong> </p >

                <p>
                    <ListGroup variant="flush">
                        <ListGroup.Item>Portuguese</ListGroup.Item>
                        <ListGroup.Item>English</ListGroup.Item>
                    </ListGroup>
                </p>

                <p><strong>Ingredients:</strong></p >
                <p>
                    <ListGroup variant="flush">
                        {meal.ingredients.map((ingredient, idx) => <Ingredient key={idx} ingredient={ingredient} />)}
                    </ListGroup>
                </p >
            </Card.Text>
        </Card.Body>
    </Card >
}


function Ingredient({ ingredient }) {
    return <ListGroup.Item>
        <div>
            <p><strong>Name:</strong> {ingredient.name}</p>
            <p><strong>Quantity:</strong> {ingredient.userQuantity.amount} grams </p>
            <p><strong>Carbs:</strong> {ingredient.userQuantity.carbs} grams</p>
        </div>
    </ListGroup.Item>
}