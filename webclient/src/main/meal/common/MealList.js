import React from 'react'

import ListGroup from 'react-bootstrap/ListGroup'
import Card from 'react-bootstrap/Card'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Tab from 'react-bootstrap/Tab'
import Nav from 'react-bootstrap/Nav'
import Container from 'react-bootstrap/Container'


const meals = Array
    .from(Array(100).keys())
    .map(aux => { return { name: aux } })

export default function MealList() {
    return <Container>
        <Tab.Container defaultActiveKey="1">
            <Row>
                <Col sm={3} className="overflow nav-tab">
                    <Nav variant="pills" className="flex-column" justify>
                        {meals.map((meal, idx) => {
                            return <Nav.Item key={idx}><Nav.Link eventKey={idx}>{meal.name}</Nav.Link> </Nav.Item>
                        })}
                    </Nav>
                </Col>
                <Col sm={9} >
                    <Tab.Content>
                        {meals.map((meal, idx) => {
                            return <Tab.Pane key={idx} eventKey={idx}>
                                <Meal />
                            </Tab.Pane>
                        })}
                    </Tab.Content>
                </Col>
            </Row>
        </Tab.Container>
    </Container>

    // return <ListGroup variant="flush"> {meals.map((meal, idx) => <Meal key={idx} meal={meal} />)} </ListGroup>
}

function Meal() {
    const meal = { name: "Apple" }

    return <Card>
        <Card.Title>{meal.name}</Card.Title>
        <Card.Body>
            Lorem ipsum
        </Card.Body>
    </Card>

}