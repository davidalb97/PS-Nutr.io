import React from 'react'

import Card from 'react-bootstrap/Card'

export default function ReportPage() {
    return <Card>
        <Card.Header as="h1">Reports</Card.Header>
        <Card.Body>
            View all reports for restaurants or meals and act on them by banning the submitter
            or deleting said submission.
            <hr />
        </Card.Body>
    </Card>
}