import React, { useContext } from 'react'
import { Link } from 'react-router-dom'

import Card from 'react-bootstrap/Card'
import Figure from 'react-bootstrap/Figure'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import Loading from '../../bootstrap-common/Loading'
import UserContext from '../../authentication/UserContext'

export default function ProfilePage() {
    const context = useContext(UserContext)

    return <Card>
        <Card.Header as="h1">Your profile</Card.Header>
        <Card.Body>
            <Body />
        </Card.Body>
    </Card>

    function Body() {
        if (!context.initialized) return <Loading />

        if (!context.user) return (
            <Row>
                You are not logged in!
                Either <Link to="/login">log in</Link>
                or <Link to="/register">register</Link> if you don't have an account.
            </Row>
        )

        return <Row>
            <Col>
                <strong>Username:</strong> {context.user.username} <p />
                <strong>Email:</strong> {context.user.email}
            </Col>
            <Col xs sm md="auto">
                <Figure>
                    <Figure.Image
                        width={512}
                        height={512}
                        alt="Profile image"
                        src={context.user.userImage}
                    />
                </Figure>
            </Col>
        </Row>
    }
}