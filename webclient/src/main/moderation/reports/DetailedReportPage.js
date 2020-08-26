import React from 'react'
import { useParams, Link } from 'react-router-dom'
import RequestingEntity from '../../common/RequestingEntity'
import useFetch, { FetchStates } from '../../common/useFetch'


import Card from 'react-bootstrap/Card'
import Button from 'react-bootstrap/Button'
import Alert from 'react-bootstrap/Alert'
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
                request={{ url: `http://localhost:9000/api/report/${submissionId}` }}
                onError={() => <Alert variant="danger">Could not obtain requested details! Please try again later.</Alert>}
                onSuccess={Report}
                onDefault={Loading}
            />
        </Card.Body>
    </Card >
}