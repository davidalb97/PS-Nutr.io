import React, { useState, useRef, useReducer, useEffect, useContext } from 'react'
import { Redirect } from 'react-router-dom'
import useFetch, { FetchStates } from '../../common/useFetch'

import Container from 'react-bootstrap/Container'
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

import { set as setAuthToken } from '../CookieService'
import UserContext from '../UserContext'

export default function RegisterPage() {
    const [credentials, setCredentials] = useState({ email: "", password: "", username: "" })
    const [request, triggerRequest] = useReducer(buildRequest, {})
    const [fetchState, response, json, error] = useFetch(request)
    const userContext = useContext(UserContext)

    const username = useRef()
    const email = useRef()
    const password = useRef()

    useEffect(() => { if (fetchState === FetchStates.done && json) setAuthToken({ value: json.jwt }) }, [fetchState, json])

    if (userContext.user) return <Redirect to="/" />
    if (fetchState === FetchStates.done && json) return <Redirect to="/" />

    return <Container className="login">
        <Form>
            <h1>Register</h1>
            <Form.Group controlId="username">
                <Form.Label>Username</Form.Label>
                <Form.Control
                    type="text"
                    placeholder="Username"
                    ref={username}
                    autoComplete="off"
                    onInput={() => onInput(username.current)}
                />
            </Form.Group>

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
                <Form.Control.Feedback type="invalid">An user already exists with that username!</Form.Control.Feedback>
            </Form.Group>

            <Form.Group controlId="password">
                <Form.Label>Password</Form.Label>
                <Form.Control type="password" placeholder="Password" ref={password} onInput={() => onInput(password.current)} />
            </Form.Group>

            <Button variant="primary" onClick={triggerRequest}>
                Register
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
            url: "http://localhost:9000/api/user/register",
            method: "POST",
            body: credentials
        }
    }
}