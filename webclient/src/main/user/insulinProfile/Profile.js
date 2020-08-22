import React, { useState, useEffect } from 'react'

import ListGroup from 'react-bootstrap/ListGroup'
import Card from 'react-bootstrap/Card'

import { endOfToday, startOfToday } from 'date-fns'
import TimeRange from 'react-timeline-range-slider'
export default function Profile({ profile, disabledIntervals }) {
    return <>
        <Card.Header as="h1">{profile.profileName}</Card.Header>
        <ListGroup.Item>
            <strong>Glucose objective: </strong>{profile.glucoseObjective} <p />
            <strong>Insulin sensitivity factor: </strong>{profile.insulinSensitivityFactor} <p />
            <strong>Carbohydrate ratio</strong>{profile.carbohydrateRatio} <p />

            <strong>Time interval:</strong><p />
            <TimeRange
                error={false}
                ticksNumber={18}
                selectedInterval={[profile.startTime, profile.endTime]}
                timelineInterval={[startOfToday(), endOfToday()]}
                disabledIntervals={disabledIntervals}
                onUpdateCallback={() => { }}
                onChangeCallback={() => { }}
            />

            <footer>
                <em >Intervals cannot be edited.</em>
            </footer>
        </ListGroup.Item>
    </>
}