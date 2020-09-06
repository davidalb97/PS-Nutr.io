import React from 'react'
import { Link } from 'react-router-dom'

import Alert from 'react-bootstrap/Alert'
import Button from 'react-bootstrap/Button'


export default function SuccessAlert({ heading, body, buttons = [{ link: "/", text: "Homepage" }] }) {
    return <Alert variant="success">
        <Alert.Heading>{heading}</Alert.Heading>
        <p>
            {body}
        </p>
        <hr />
        <div className="d-flex justify-content-end">
            {buttons.map((button, idx) => (
                <Link key={idx} to={button.link}>
                    <Button variant="success-outline">{button.text}</Button>
                </Link>
            ))}
        </div>
    </Alert >
}