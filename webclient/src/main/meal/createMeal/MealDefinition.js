import React, { useState, useRef, useEffect } from 'react'

import Form from 'react-bootstrap/Form'
import InputGroup from 'react-bootstrap/InputGroup'
import DropdownButton from 'react-bootstrap/DropdownButton'
import Dropdown from 'react-bootstrap/Dropdown'
export default function MealDefinition({ setCanAdvance, setMeal }) {
    const [weightUnit, setWeightUnit] = useState("grams")
    const [mealDefinition, setMealDefinition] = useState({ quantity: undefined, name: undefined })

    //References for form inputs in order to access values
    const mealNameInput = useRef()
    const quantityInput = useRef()

    useEffect(() => {
        const canAdvance = allInputsValid()

        setCanAdvance(canAdvance)
        if (canAdvance) setMeal(mealDefinition)

    }, [mealDefinition])

    function handleInputChange(event) {
        const newDefinition = { ...mealDefinition }
        newDefinition[event.id] = event.value
        setMealDefinition(newDefinition)
    }

    /**
     * Returns true if all field values are defined
     */
    function allInputsValid() {
        return !Object.values(mealDefinition).some(field => !field || field === '')
    }

    //TODO Add ounce <-> gram conversion
    return <Form>
        <Form.Group controlId="name">
            <Form.Label>Meal name</Form.Label>
            <Form.Control
                ref={mealNameInput}
                type="text"
                onChange={() => handleInputChange(mealNameInput.current)}
            />
        </Form.Group>

        <Form.Group controlId="quantity">
            <Form.Label>Quantity</Form.Label>
            <Form.Control
                ref={quantityInput}
                type="number"
                onChange={() => handleInputChange(quantityInput.current)}
            />

            <DropdownButton
                as={InputGroup.Append}
                variant="outline-secondary"
                title={weightUnit}
                id="input-group-dropdown-2"
            >

                <Dropdown.Item onClick={() => setWeightUnit("grams")}>  grams  </Dropdown.Item>
                <Dropdown.Item onClick={() => setWeightUnit("ounces")}> ounces </Dropdown.Item>
            </DropdownButton>
        </Form.Group>

    </Form>
}