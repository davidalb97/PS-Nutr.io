import React, { useRef, useState, useEffect, useCallback, useReducer } from 'react'

import Form from 'react-bootstrap/Form'
import ListGroup from 'react-bootstrap/ListGroup'
import Card from 'react-bootstrap/Card'
export default function Portions({ setCanAdvance, setMeal, meal }) {
    function updateQuantity(newQuantity) {
        //Where 'this' is Ingredient
        this.userQuantity = newQuantity
        setMeal(meal)
    }

    useEffect(() => {
        function reducer(sumQuantity, ingredient) {
            return + ingredient.userQuantity.amount + sumQuantity
        }
        const canAdvance = meal.ingredients.reduce(reducer, 0) <= meal.quantity
        setCanAdvance(canAdvance)

    }, [meal, setCanAdvance])


    const ingredients = meal.ingredients.map((ingredient, idx) => {
        return <IngredientPortion
            key={idx}
            ingredient={ingredient}
            onChange={updateQuantity.bind(ingredient)}
            maxQuantity={meal.quantity}
        />
    })

    return <>
        Set the quantity that each meal will have:
        <ListGroup>
            {ingredients}
        </ListGroup>
    </>
}

function IngredientPortion({ ingredient, onChange, maxQuantity }) {
    const [nutritionalInfo, setNutritionalInfo] = useState(ingredient.userQuantity)
    const quantity = useRef()

    function onQuantityChange() {
        const newNutrition = {
            unit: nutritionalInfo.unit,
            amount: quantity.current.value,
            carbs: carbTool(ingredient.nutritionalInfo.carbs, ingredient.nutritionalInfo.amount, quantity.current.value)
        }

        setNutritionalInfo(newNutrition)
        onChange(newNutrition)
    }

    return <ListGroup.Item>
        <Card>
            <Card.Title> {ingredient.name} </Card.Title>
            <Card.Body>
                Carbs: {nutritionalInfo.carbs}
                <Form.Group controlId={ingredient.id}>
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

        </Card>
    </ListGroup.Item>
}


function carbTool(baseCarbs, quantity, newQuantity) {
    const result = (newQuantity * baseCarbs) / quantity
    return Math.floor(result)
}