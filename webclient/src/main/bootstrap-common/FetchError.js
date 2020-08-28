import React from 'react'
import { Link } from 'react-router-dom'

import Alert from 'react-bootstrap/Alert'
import Button from 'react-bootstrap/Alert'

export default function FetchError({ json, error }) {
    const title = json && json.title ? json.title : "Failed to complete your request!"
    const detail = json && json.detail ? json.detail : "You should try again later or contact us if the problem persists."

    return <Alert variant="danger">
        <Alert.Heading>{title}</Alert.Heading>
        <p>{detail}</p>
        <hr />
        <div className="d-flex justify-content-end">
            <Link to="/">
                <Button variant="outline-danger">Back to homepage</Button>
            </Link>
        </div>
    </Alert >
}