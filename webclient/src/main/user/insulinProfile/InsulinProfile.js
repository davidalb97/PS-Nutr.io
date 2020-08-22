import React, { useState, useContext, useEffect, useRef } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'

import Card from 'react-bootstrap/Card'
import ListGroup from 'react-bootstrap/ListGroup'
import Spinner from 'react-bootstrap/Spinner'
import Alert from 'react-bootstrap/Alert'
import Button from 'react-bootstrap/Button'

import UserContext from '../../authentication/UserContext'
import NewProfile from './NewProfile'
import Profile from './Profile'

import { getTodayAtSpecificHour } from '../../utils/DateUtils'

const NO_PROFILES_MSG = "You have no profiles! Let's start by creating one."
const HAS_PROFILES_MSG = "Your insulin profiles:"

const staticProfiles = [
    { profileName: "Profile 1", startTime: "12:00", endTime: "20:00" },
    { profileName: "Profile 2", startTime: "10:00", endTime: "12:00" },
    { profileName: "Profile 3", startTime: "20:00", endTime: "23:00" }
]

export default function InsulinProfile() {
    const [fetchState, response, json, error] = useFetch({
        url: "http://localhost:9000/api/user/profile",
        authToken: useContext(UserContext).authToken
    })

    const [profiles, updateProfiles] = useState([])
    const [newProfileView, setNewProfileView] = useState(null)

    useEffect(() => {
        // if (json) updateProfiles(json)
        if (json) updateProfiles(staticProfiles)
    }, [json])

    return <Card>
        <Card.Header as="h1">Insulin Profile</Card.Header>
        <Card.Body>
            An insulin profile allows you to use our insulin calculator in order to automatically calculate the amount of insulin units needed
            when eating meals at a restaurant.
            <hr />
            <Selector />
        </Card.Body>
    </Card>

    function Selector() {
        switch (fetchState) {
            case FetchStates.fetching: return <Loading />
            case FetchStates.error: return <Error />
            case FetchStates.done: return <ProfileList />
            default: return null
        }
    }

    function ProfileList() {
        return <>
            <strong>{profiles.length > 0 ? HAS_PROFILES_MSG : NO_PROFILES_MSG}</strong>
            <p />
            <ListGroup>
                {profiles.map((profile, idx) => <Profile key={idx} profile={profile} disabledIntervals={sumDisabledIntervals()} />)}
                {newProfileView}
            </ListGroup>
            <p />
            {newProfileView != null ?
                <> </> :
                <Button
                    variant="outline-primary"
                    onClick={() => setNewProfileView(
                        <NewProfile
                            onProfileCreation={onProfileCreation}
                            disabledIntervals={sumDisabledIntervals()}
                        />
                    )}
                    block
                >
                    Create new profile
            </Button>
            }
        </>
    }

    function Error() {
        return <Alert variant="danger">
            <Alert.Heading>Failed to obtain your insulin profiles!</Alert.Heading>
            <p className="mb-0">
                We currently can't access your insulin profiles. Maybe try again later?
            </p>
        </Alert>
    }

    function Loading() {
        return <Spinner animation="border" role="status"><span className="sr-only">Loading...</span></Spinner>
    }

    //---------
    //
    function onProfileCreation({ profile }) {
        if (profile) updateProfiles([...profiles, profile])
        setNewProfileView(null)
    }

    function sumDisabledIntervals() {
        function reducer(intervals, profile) {
            return [...intervals, {
                start: getTodayAtSpecificHour(profile.startTime.match(/.+?(?=:)/)[0]),
                end: getTodayAtSpecificHour(profile.endTime.match(/.+?(?=:)/)[0])
            }]
        }
        return profiles.reduce(reducer, [])
    }
}