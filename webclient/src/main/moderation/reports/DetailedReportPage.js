import React from 'react'
import { useParams, Link } from 'react-router-dom'
import RequestingEntity from '../../common/RequestingEntity'


import Card from 'react-bootstrap/Card'
import Button from 'react-bootstrap/Button'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Loading from '../../bootstrap-common/Loading'
import Report from './Report'

export default function DetailedReportPage() {
    const { submissionId } = useParams()

    return <Card>
        <Card.Header as="h1">
            <Row>
                <Col>Reports - Detailed view</Col>
                <Col xs sm md="auto">
                    <Link to="/moderation/reports">
                        <Button variant="info"> Back to all reports </Button>
                    </Link>
                </Col>
            </Row>
        </Card.Header>
        <Card.Body>
            <RequestingEntity
                request={{ url: `http://localhost:8080/report/${submissionId}` }}
                onSuccess={({ json }) => { return <Report report={json} /> }}
                onDefault={Loading}
            />
        </Card.Body>
    </Card >
}