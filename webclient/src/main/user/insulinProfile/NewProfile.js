import React, { useState, useRef, useContext, useEffect } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'

import ListGroup from 'react-bootstrap/ListGroup'
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'
import OverlayTrigger from 'react-bootstrap/OverlayTrigger'
import Popover from 'react-bootstrap/Popover'
import Alert from 'react-bootstrap/Alert'

import { endOfToday, startOfToday, areIntervalsOverlapping } from 'date-fns'
import TimeRange from 'react-timeline-range-slider'

import UserContext from '../../authentication/UserContext'

const defaultNewProfile = {
    profileName: undefined,
    startTime: startOfToday(),
    endTime: endOfToday(),
    glucoseObjective: undefined,
    insulinSensitivityFactor: undefined,
    carbohydrateRatio: undefined,
    onCreationPhase: true
}

export default function NewProfile({ onProfileCreation, disabledIntervals }) {
    const [status, setStatus] = useState({
        profile: defaultNewProfile,
        hasUpdated: false,
        request: {},
        timesCollide: false,
        isProfileValid: false
    })

    const authToken = useContext(UserContext).authToken
    const profileName = useRef()
    const glucoseObjective = useRef()
    const carbRatio = useRef()
    const insulinSensitivityFactor = useRef()

    const [fetchState, response, json, error] = useFetch(status.request)
    //Check if profile is valid when profile changes
    useEffect(() => {
        const timesCollide = disabledIntervals.some(disabledInterval => areIntervalsOverlapping(
            disabledInterval,
            { start: status.profile.startTime, end: status.profile.endTime }
        ))

        const isProfileValid = !status.timesCollide
            && status.profile.profileName && status.profile.name !== ''
            && status.profile.glucoseObjective > 0
            && status.profile.insulinSensitivityFactor > 0
            && status.profile.carbohydrateRatio > 0

        setStatus({ ...status, isProfileValid: isProfileValid, timesCollide: timesCollide })
    }, [status.profile])

    if (fetchState === FetchStates.error) {
        return <ListGroup.Item>
            <ErrorAlert />
        </ListGroup.Item>
    }

    if (fetchState === FetchStates.done) {
        return <ListGroup.Item>
            <SuccessAlert />
        </ListGroup.Item>
    }

    return <ListGroup.Item>
        <Form>
            <Form.Group controlId="profileName">
                <Form.Label>Name (optional)</Form.Label>
                <Form.Control
                    value={status.profile.profileName}
                    onChange={() => onInput(profileName.current)}
                    ref={profileName}
                    autoComplete="off"
                    type="text"
                />
            </Form.Group>
            <Form.Group controlId="glucoseObjective">
                <Form.Label>Glucose objective</Form.Label>
                <Form.Control
                    onChange={() => onInput(glucoseObjective.current)}
                    ref={glucoseObjective}
                    type="number"
                    value={status.profile.glucoseObjective}
                />
            </Form.Group>
            <Form.Group controlId="carbohydrateRatio">
                <Form.Label>Carbohydrate ratio</Form.Label>
                <Form.Control
                    onChange={() => onInput(carbRatio.current)}
                    ref={carbRatio}
                    type="number"
                    value={status.profile.carbohydrateRatio}
                />
            </Form.Group>
            <Form.Group controlId="insulinSensitivityFactor">
                <Form.Label>Sensitivity factor</Form.Label>
                <Form.Control
                    onChange={() => onInput(insulinSensitivityFactor.current)}
                    ref={insulinSensitivityFactor}
                    type="number"
                    value={status.profile.insulinSensitivityFactor}
                />
            </Form.Group>
            <p />
            <TimeRange
                error={status.timesCollide}
                ticksNumber={18}
                selectedInterval={[status.profile.startTime, status.profile.endTime]}
                timelineInterval={[startOfToday(), endOfToday()]}
                disabledIntervals={disabledIntervals}
                onUpdateCallback={() => { }}
                onChangeCallback={onChangeCallback}
            />
            <p />
            <ActionButton />
        </Form>
    </ListGroup.Item>


    function onInput(ref) {
        const updatedProfile = { ...status.profile }
        updatedProfile[ref.id] = ref.value
        setStatus({ ...status, hasUpdated: true, profile: updatedProfile })
    }

    function onChangeCallback(selectedInterval) {
        onInput({ id: "startTime", value: selectedInterval[0] })
        onInput({ id: "endTime", value: selectedInterval[1] })
    }

    function ActionButton() {
        return <>
            <Button variant="danger" onClick={onProfileCreation} block>
                Delete
            </Button>
            <p />
            <RequestButton />
        </>

        function RequestButton() {
            let text
            let disabled
            let pointerEvent
            let ErrorToolTip = ({ children }) => <>{children} </>

            switch (fetchState) {
                case FetchStates.fetching: {
                    text = "Loading..."
                    disabled = true
                    pointerEvent = 'none'
                }

                case FetchStates.done: {
                    text = "Created with success!"
                    disabled = true
                    pointerEvent = 'none'
                }

                default: {
                    text = "Create"
                    disabled = !status.isProfileValid
                    pointerEvent = status.isProfileValid ? 'auto' : 'none'

                    if (disabled) {
                        const popover = <Popover id={`popover-positioned-top`}>
                            <Popover.Title as="h2">Invalid fields</Popover.Title>
                            <Popover.Content>
                                Make sure your time range is valid and that
                                all mandatory fields are present.
                        </Popover.Content>
                        </Popover>

                        ErrorToolTip = ({ children }) => <OverlayTrigger placement="top" overlay={popover}>
                            {children}
                        </OverlayTrigger>
                    }
                }
            }

            return <ErrorToolTip>
                <span >
                    <Button
                        variant="primary"
                        onClick={triggerRequest}
                        disabled={disabled}
                        style={{ pointerEvents: pointerEvent }}
                        block
                    >
                        {text}
                    </Button>
                </span>
            </ErrorToolTip>

            function triggerRequest() {
                setStatus({
                    ...status,
                    request: {
                        ...status.request,
                        method: "POST",
                        authToken: authToken,
                        url: "http://localhost:9000/api/user/profile",
                        body: status.profile
                    }
                })
            }
        }
    }

    function ErrorAlert() {
        return <Alert variant="danger">
            <Alert.Heading>Failed to create insulin profile!</Alert.Heading>
            <p>
                You should try again later.
            </p>
            <hr />
            <div className="d-flex justify-content-end">
                <Button onClick={onProfileCreation} variant="outline-danger">
                    Close
                </Button>
            </div>
        </Alert >
    }

    function SuccessAlert() {
        return <Alert show={show} variant="success">
            <Alert.Heading>Added meal with success!</Alert.Heading>
            <p>
                You can close this menu and continue editing your profiles.
            </p>
            <hr />
            <div className="d-flex justify-content-end">
                <Button onClick={() => { onProfileCreation({ profile: status.profile }) }} variant="outline-sucess">
                    Close
          </Button>
            </div>
        </Alert >
    }
}

function toString(date) {
    return `${date.getHours()}:00`
}