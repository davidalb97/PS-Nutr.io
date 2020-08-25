import React, { useRef, useEffect, useState } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'

import Card from 'react-bootstrap/Card'
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'
import Form from 'react-bootstrap/Form'
import InputGroup from 'react-bootstrap/InputGroup'
import Button from 'react-bootstrap/Button'
import Overlay from 'react-bootstrap/Overlay'
import OverlayTrigger from 'react-bootstrap/OverlayTrigger'
import Popover from 'react-bootstrap/Popover'
import PopoverContent from 'react-bootstrap/PopoverContent'
import PopoverTitle from 'react-bootstrap/PopoverTitle'

import { isNotEmpty, isPositive } from '../../forms/Validations'
import FormWrapper, { InputController } from '../../forms/FormWrapper'
import MultiSelectWrapper from '../../forms/MultiSelectWrapper'

export default function AddFoodForm({ cuisines }) {
    const [controller, body, allFieldsAreValid] = InputController()
    const [request, setRequest] = useState({})
    const [displayErrors, setDisplayErrors] = useState(false)
    const [fetchState, response, json, error] = useFetch(request)


    //Special form references need to be registered in the controller
    const foodType = { reference: useRef(), id: "foodTypes", initialValue: "Ingredient" }
    useEffect(() => { controller.register(foodType) }, [])


    const options = cuisines.map(cuisine => { return { label: cuisine, value: cuisine } })
    return <Card>
        <Card.Header as="h1">Suggested food creation</Card.Header>
        <Card.Body>
            Creates a new meal or ingredient that is suggested to users when searching for meals served in a restaurant
            or when creating a custom meal.
            <hr />
            <Form>
                <Row>
                    <Col>
                        <Form.Group>
                            <Form.Label>Food name</Form.Label>
                            <FormWrapper
                                id="name"
                                type="text"
                                autoComplete="off"
                                validators={[isNotEmpty]}
                                controller={controller}
                                displayErrorTooltipOnEmpty={displayErrors}
                            />
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group controlId={foodType.id}>
                            <Form.Label>Food type</Form.Label>
                            <Form.Control
                                as="select"
                                defaultValue={foodType.initialValue}
                                ref={foodType.reference}
                                onChange={() => { controller.onChange(foodType.id, foodType.reference.current.value) }}
                            >
                                <option>Ingredient</option>
                                <option>Meal</option>
                            </Form.Control>
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col>
                        <Form.Group>
                            <Form.Label>Quantity</Form.Label>
                            <InputGroup className="mb-3">

                                <FormWrapper
                                    id="quantity"
                                    type="number"
                                    validators={[isPositive]}
                                    controller={controller}
                                    displayErrorTooltipOnEmpty={displayErrors}
                                />
                                <InputGroup.Append>
                                    <InputGroup.Text id="basic-addon2">gr</InputGroup.Text>
                                </InputGroup.Append>
                            </InputGroup>
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group>
                            <Form.Label>Carbohydrates</Form.Label>
                            <InputGroup className="mb-3">
                                <FormWrapper
                                    id="carbs"
                                    type="number"
                                    validators={[isPositive]}
                                    controller={controller}
                                    displayErrorTooltipOnEmpty={displayErrors}

                                />
                                <InputGroup.Append>
                                    <InputGroup.Text id="basic-addon2">gr</InputGroup.Text>
                                </InputGroup.Append>
                            </InputGroup>
                        </Form.Group>
                    </Col>
                </Row>

                <p />
                <Row>
                    <Col xs md={"auto"}>
                        <InputGroup.Append>
                            <InputGroup.Text id="basic-addon2">Cuisines</InputGroup.Text>
                        </InputGroup.Append>
                    </Col>

                    <Col>
                        <MultiSelectWrapper
                            id="cuisines"
                            options={options}
                            controller={controller}
                        />
                    </Col>
                </Row>

                {/* ---------------------------- */}
                <hr />
                <RequestButton />
            </Form>
        </Card.Body>
    </Card >


    function RequestButton() {
        let text = "Submit"
        let disabled = false
        let Tooltip = allFieldsAreValid ? ({ children }) => <> {children} </> : ErrorTooltip

        return <Tooltip>
            <div >
                <Button
                    variant="primary"
                    onClick={triggerRequest}
                    disabled={disabled}
                    block
                >
                    {text}
                </Button>
            </div>
        </Tooltip>
    }

    function ErrorTooltip({ children }) {
        const popover = (
            <Popover id={`popover-positioned-top`}>
                <Popover.Title as="h2">Invalid fields</Popover.Title>
                <Popover.Content>
                    Make sure all fields are valid and at least one cuisine is selected.
                </Popover.Content>
            </Popover>)

        return <OverlayTrigger placement="top" overlay={popover} trigger="focus">
            {children}
        </OverlayTrigger>
    }


    function triggerRequest() {
        console.log(body)
        if (!allFieldsAreValid) return setDisplayErrors(true)
    }
}