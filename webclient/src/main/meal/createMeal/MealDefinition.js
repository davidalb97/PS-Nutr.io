import React, { useState, useRef, useEffect } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'

import Form from 'react-bootstrap/Form'
import InputGroup from 'react-bootstrap/InputGroup'
import DropdownButton from 'react-bootstrap/DropdownButton'
import Dropdown from 'react-bootstrap/Dropdown'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import MultiSelect from "react-multi-select-component"
import FetchError from '../../bootstrap-common/FetchError'
import Loading from '../../bootstrap-common/Loading'

import { isNotEmpty, isPositive } from '../../forms/Validations'

export default function MealDefinition({ setCanAdvance, setMeal, meal }) {
    const [mealDefinition, setMealDefinition] = useState({
        quantity: meal.quantity,
        name: meal.name,
        unit: meal.unit || "gr",
        cuisines: meal.cuisines || []
    })
    const [fetchState, response, json, error] = useFetch({ url: "http://localhost:9000/api/cuisine" })

    //References for form inputs in order to access values
    const mealNameInput = useRef()
    const quantityInput = useRef()

    useEffect(() => {
        const canAdvance = allInputsValid()

        setCanAdvance(canAdvance)
        if (canAdvance) setMeal(mealDefinition)

    }, [mealDefinition])

    //TODO Add ounce <-> gram conversion

    if (fetchState === FetchStates.error) return <FetchError json={json} error={error} />
    if (fetchState === FetchStates.fetching || !json) return <Loading />

    const options = json.cuisines.map(cuisine => { return { label: cuisine, value: cuisine } })
    return <Form>
        <Form.Group controlId="name">
            <Form.Label>Meal name</Form.Label>
            <Form.Control
                ref={mealNameInput}
                type="text"
                onChange={() => handleInputChange(mealNameInput.current)}
                value={mealDefinition.name || ""}
                autoComplete="off"
            />
        </Form.Group>

        <Form.Group controlId="quantity">
            <Form.Label>Quantity</Form.Label>
            <InputGroup>
                <Form.Control
                    ref={quantityInput}
                    type="number"
                    onChange={() => handleInputChange(quantityInput.current)}
                    value={mealDefinition.quantity}
                />

                <DropdownButton
                    as={InputGroup.Append}
                    variant="outline-secondary"
                    title={mealDefinition.unit}
                    id="input-group-dropdown-2"
                >

                    <Dropdown.Item onClick={() => setMealDefinition({ ...mealDefinition, unit: "gr" })}>grams </Dropdown.Item>
                    <Dropdown.Item onClick={() => setMealDefinition({ ...mealDefinition, unit: "oz" })}>ounces</Dropdown.Item>
                </DropdownButton>
            </InputGroup>
        </Form.Group>

        <Row>
            <Col className="col-2">
                <InputGroup.Append className="multiselect-cta">
                    <InputGroup.Text className="justify-content-center">Cuisines</InputGroup.Text>
                </InputGroup.Append>
            </Col>
            <Col className="col-10">
                <MultiSelect
                    options={options}
                    value={mealDefinition.cuisines}
                    onChange={(selected) => setMealDefinition({ ...mealDefinition, cuisines: selected })}
                    labelledBy={"Select..."}
                />
            </Col>
        </Row>
    </Form>

    function handleInputChange(event) {
        const newDefinition = { ...mealDefinition }
        newDefinition[event.id] = event.value
        setMealDefinition(newDefinition)
    }

    /**
     * Returns true if all field values are defined
     */
    function allInputsValid() {
        return isNotEmpty(mealDefinition.name) &&
            isPositive(mealDefinition.quantity) &&
            mealDefinition.cuisines.length > 0
    }
}