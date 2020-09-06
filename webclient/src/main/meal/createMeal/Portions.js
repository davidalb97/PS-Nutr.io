import React, { useRef, useState, useEffect } from 'react'

import Form from 'react-bootstrap/Form'
import ListGroup from 'react-bootstrap/ListGroup'
import Card from 'react-bootstrap/Card'
export default function Portions({ setCanAdvance, setMeal, meal }) {
    const [components] = useState(meal
        .ingredients
        .concat(meal.meals)
        .map(component => component.value)
    )

    useEffect(() => {
        function reducer(sumQuantity, component) {
            return + component.userQuantity.amount + sumQuantity
        }
        const quantitySum = components.reduce(reducer, 0)

        const canAdvance = quantitySum <= meal.quantity && quantitySum > 0
        setCanAdvance(canAdvance)
    }, [meal, setCanAdvance])

    const componentList = components.map((component, idx) => {
        return <ComponentPortion
            key={idx}
            component={component}
            onChange={updateQuantity.bind(component)}
            maxQuantity={meal.quantity}
        />
    })

    return <>
        <h4>Set the quantity that each component will have:</h4>
        <p />
        <ListGroup>
            {componentList}
        </ListGroup>
    </>


    function updateQuantity(newQuantity) {
        //Where 'this' is food component
        this.userQuantity = newQuantity
        setMeal(meal)
    }
}

function ComponentPortion({ component, onChange, maxQuantity }) {
    const [nutritionalInfo, setNutritionalInfo] = useState(component.userQuantity)
    const quantity = useRef()

    function onQuantityChange() {
        const newNutrition = {
            unit: nutritionalInfo.unit,
            amount: quantity.current.value,
            carbs: carbTool(component.nutritionalInfo.carbs, component.nutritionalInfo.amount, quantity.current.value)
        }

        setNutritionalInfo(newNutrition)
        onChange(newNutrition)
    }

    return <ListGroup.Item>
        <Card.Title> {component.name} </Card.Title>
        <Card.Body>
            Carbs: {nutritionalInfo.carbs}
            <Form.Group controlId={component.id}>
                <Form.Label>Quantity: {nutritionalInfo.amount}</Form.Label>
                <Form.Control
                    ref={quantity}
                    type="range"
                    value={nutritionalInfo.amount}
                    onChange={onQuantityChange}
                    max={Number(maxQuantity)}
                />
            </Form.Group>
        </Card.Body>
    </ListGroup.Item>
}


function carbTool(baseCarbs, quantity, newQuantity) {
    const result = (newQuantity * baseCarbs) / quantity
    return Math.floor(result)
}