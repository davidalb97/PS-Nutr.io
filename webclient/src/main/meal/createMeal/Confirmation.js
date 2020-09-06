import React, { useEffect, useState } from 'react'

import Card from 'react-bootstrap/Card'
import ListGroup from 'react-bootstrap/ListGroup'
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'


export default function Confirmation({ setCanAdvance, meal }) {
    useEffect(() => { setCanAdvance(true) }, [setCanAdvance])

    const [totalCarbs] = useState(meal
        .ingredients
        .concat(meal.meals)
        .map(component => component.value.userQuantity)
        .reduce((total, quantity) => + quantity.carbs + total, 0)
    )

    return <Card>
        <Card.Header as="h1">Confirm your meal:</Card.Header>
        <Card.Body>
            <Card.Text>
                <p><strong>Name:</strong> {meal.name} </p >
                <p><strong>Quantity:</strong> {meal.quantity} grams </p>
                <p><strong>Total carbs:</strong> {totalCarbs} grams</p >
                <p>
                    <strong>Cuisines: </strong> {meal
                        .cuisines
                        .map(cuisine => cuisine.label)
                        .join(', ')
                    }
                </p>
                <ListComponents title="meals" components={meal.meals} />
                <ListComponents title="ingredients" components={meal.ingredients} />
            </Card.Text>
        </Card.Body>
    </Card >
}

function ListComponents({ title, components }) {
    if (components.length <= 0) return <> </>
    return <>
        <p><strong>Composed by the following {title}:</strong></p >
        <p>
            <ListGroup variant="flush">
                {components
                    .map(food => food.value)
                    .map((component, idx) => <Component key={idx} component={component} />)
                }
            </ListGroup>
        </p >
    </>
}


function Component({ component }) {
    return <ListGroup.Item>
        <span>
            {component.userQuantity.amount}{component.userQuantity.unit} of <strong>{component.name}</strong> with a total of {component.userQuantity.carbs}gr of carbohydrates
        </span>
    </ListGroup.Item>
}