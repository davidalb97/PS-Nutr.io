import React, { useState, useReducer } from 'react'

import ListGroup from 'react-bootstrap/ListGroup'
import Card from 'react-bootstrap/Card'
import Button from 'react-bootstrap/Button'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Alert from 'react-bootstrap/Alert'


import { endOfToday, startOfToday } from 'date-fns'
import TimeRange from 'react-timeline-range-slider'
import RequestingEntity from '../../common/RequestingEntity'

export default function Profile({ profile, disabledIntervals, onProfileDeletion }) {
    const [request, triggerRequest] = useReducer(_ => {
        return {
            url: `/user/profile/${profile.name}`,
            method: "DELETE"
        }
    }, {})

    return <RequestingEntity
        request={request}
        onDefault={DisplayProfile}
        onSuccess={SuccessAlert}
    />

    function DisplayProfile() {
        return <>
            <Card.Header as="h3">
                <Row>
                    <Col>{profile.name}</Col>
                    <Col className="d-flex justify-content-end">
                        <Button variant="danger" onClick={triggerRequest} >Delete</Button>
                    </Col>
                </Row>
            </Card.Header>
            <ListGroup.Item>
                <span>
                    <strong>Glucose objective: </strong>{profile.glucoseObjective} <p />
                    <strong>Insulin sensitivity factor: </strong>{profile.sensitivityFactor} <p />
                    <strong>Carbohydrate ratio: </strong>{profile.carbohydrateRatio} <p />

                    <strong>Time interval: </strong> {profile.startTime}h - {profile.endTime}h
            </span>
                <p />
                <TimeRange
                    error={false}
                    ticksNumber={18}
                    selectedInterval={[profile.startTime, profile.endTime]}
                    timelineInterval={[startOfToday(), endOfToday()]}
                    disabledIntervals={disabledIntervals}
                    onUpdateCallback={() => { }}
                    onChangeCallback={() => { }}
                />
            </ListGroup.Item>
        </>
    }

    function SuccessAlert() {
        return <Alert variant="success">
            <Alert.Heading>Deleted meal with success!</Alert.Heading>
            <p>
                You can close this menu and continue editing your profiles.
            </p>
            <hr />
            <div className="d-flex justify-content-end">
                <Button
                    onClick={onProfileDeletion}
                    variant="outline-sucess"
                >
                    Close
                </Button>
            </div>
        </Alert >
    }
}