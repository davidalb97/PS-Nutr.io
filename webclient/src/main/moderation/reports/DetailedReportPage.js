import React, { useState } from 'react'
import { useParams } from 'react-router-dom'
import useFetch, { FetchStates } from '../../common/useFetch'

import Card from 'react-bootstrap/Card'
import Tabs from 'react-bootstrap/Tabs'
import Tab from 'react-bootstrap/Tab'
import ListGroup from 'react-bootstrap/ListGroup'
import Button from 'react-bootstrap/Button'
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Alert from 'react-bootstrap/Alert'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Loading from '../../bootstrap-common/Loading'
import Report from './Report'

export default function DetailedReportPage() {
    const { submissionId } = useParams()
    const [fetchState, response, json, error] = useFetch({ url: `http://localhost:9000/api/report/${submissionId}` })


    return <Card>
        <Card.Header as="h1">
            <Row>
                <Col>Reports - Detailed view</Col>
                <Col xs sm md="auto"><Button variant="info">Back to all reports</Button></Col>
            </Row>
        </Card.Header>
        <Card.Body>
            <Body />
        </Card.Body>
    </Card>

    function Body() {
        switch (fetchState) {
            case FetchStates.error: return <>
                <Alert variant="danger">Could not obtain requested details! Please try again later.</Alert>
            </>

            case FetchStates.done: return <Report report={json} />
            case FetchStates.fetching: return <Loading />
            default: return <Loading />
        }
    }
}