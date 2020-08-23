import React, { useContext, useRef } from 'react'

import Card from 'react-bootstrap/Card'
import Col from 'react-bootstrap/Col'
import Row from 'react-bootstrap/Row'
import Form from 'react-bootstrap/Form'
import ListGroup from 'react-bootstrap/ListGroup'
import Container from 'react-bootstrap/Container'

import UserContext from '../authentication/UserContext'
export default function AddSuggestedFood() {
    const user = useContext(UserContext)

    //TODO Check if user is allowed to view resource
    const name = useRef()
    const carbs = useRef()
    const quantity = useRef()
    const foodType = useRef()
    const cuisines = useRef()

    return <Card>
        <Card.Header as="h1">Suggested food creation</Card.Header>
        <Card.Body>
            Creates a new meal or ingredient that is suggested to users when searching for meals served in a restaurant
            or when creating a custom meal.
            <hr />
            <Form>
                <Row>
                    <Col>
                        <Form.Group controlId="formFoodName">
                            <Form.Label>Food name</Form.Label>
                            <Form.Control
                                onChange={() => onInput(name.current)}
                                ref={name}
                                type="text"
                            />
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group controlId="formFoodType">
                            <Form.Label>Food type</Form.Label>
                            <Form.Control
                                as="select"
                                defaultValue="Choose..."
                                onChange={() => onInput(foodType.current)}
                                ref={foodType}
                            >
                                <option>Ingredient</option>
                                <option>Meal</option>
                            </Form.Control>
                        </Form.Group>
                    </Col>
                </Row>

                <Row>
                    <Col>
                        <Form.Group controlId="formFoodName">
                            <Form.Label>Quantity</Form.Label>
                            <Form.Control
                                onChange={() => onInput(quantity.current)}
                                ref={quantity}
                                type="number"
                                label={"gr"}
                            />
                        </Form.Group>
                    </Col>
                    <Col>
                        <Form.Group controlId="formFoodName">
                            <Form.Label>Carbohydrates</Form.Label>
                            <Form.Control
                                onChange={() => onInput(carbs.current)}
                                ref={carbs}
                                type="number"
                            />
                        </Form.Group>
                    </Col>
                </Row>

                
            </Form>
        </Card.Body>
    </Card>


    function onInput(ref) {
        // const updatedProfile = { ...status.profile }
        // updatedProfile[ref.id] = ref.value
        // setStatus({ ...status, hasUpdated: true, profile: updatedProfile })
    }
}