import React, { useState, useRef, useReducer, useEffect, useContext } from 'react'
import { Redirect } from 'react-router-dom'
import useFetch, { FetchStates } from '../../common/useFetch'

import Container from 'react-bootstrap/Container'
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

import { set as setAuthToken } from '../CookieService'
import UserContext from '../UserContext'

export default function LoginPage() {
    const [credentials, setCredentials] = useState({ email: "", password: "" })
    const [request, triggerRequest] = useReducer(buildRequest, {})
    const [fetchState, response, json, error] = useFetch(request)
    const user = useContext(UserContext)

    const email = useRef()
    const password = useRef()

    useEffect(() => { if (fetchState === FetchStates.done && json) setAuthToken({ value: json.jwt }) }, [fetchState, json])

    if (!user.notInitialized) return <Redirect to="/" />

    return <Container className="login">
        <Form>
            <h1>Please enter login credentials</h1>
            <Form.Group controlId="email">
                <Form.Label>Email address</Form.Label>
                <Form.Control
                    type="email"
                    placeholder="Email address"
                    ref={email}
                    autoComplete="off"
                    onInput={() => onInput(email.current)}
                    isInvalid={fetchState === FetchStates.error && json}
                />
                <Form.Control.Feedback type="invalid">Given credentials are not valid!</Form.Control.Feedback>
            </Form.Group>

            <Form.Group controlId="password">
                <Form.Label>Password</Form.Label>
                <Form.Control
                    type="password"
                    placeholder="Password"
                    ref={password} onInput={() => onInput(password.current)}
                    isInvalid={fetchState === FetchStates.error && json}
                />
            </Form.Group>

            <Button variant="primary" onClick={triggerRequest}>
                Login
            </Button>
        </Form>
    </Container>

    function onInput(ref) {
        const newCredentials = { ...credentials }
        newCredentials[ref.id] = ref.value
        setCredentials(newCredentials)
    }

    function buildRequest() {
        return {
            url: "http://localhost:9000/api/user/login",
            method: "POST",
            body: credentials
        }
    }
}