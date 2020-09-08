import React, { useState, useContext, useEffect, useRef } from 'react'
import useFetch, { FetchStates } from '../../common/useFetch'
import RequestingEntity from '../../common/RequestingEntity'


import Card from 'react-bootstrap/Card'
import ListGroup from 'react-bootstrap/ListGroup'
import Spinner from 'react-bootstrap/Spinner'
import Alert from 'react-bootstrap/Alert'
import Button from 'react-bootstrap/Button'

import NewProfile from './NewProfile'
import Profile from './Profile'

import Loading from '../../bootstrap-common/Loading'

import { getTodayAtSpecificHour } from '../../utils/DateUtils'

const NO_PROFILES_MSG = "You have no profiles! Let's start by creating one."
const HAS_PROFILES_MSG = "Your insulin profiles:"
export default function InsulinProfile() {
    return <Card>
        <Card.Header as="h1">Insulin Profile</Card.Header>
        <Card.Body>
            An insulin profile allows you to use our insulin calculator in order to automatically calculate the amount of insulin units needed
            when eating meals at a restaurant.
            <hr />
            <RequestingEntity
                request={{ url: "/user/profile" }}
                onDefault={Loading}
                onSuccess={ProfileList}
            />
        </Card.Body>
    </Card>
}


function ProfileList({ json }) {
    const [profiles, updateProfiles] = useState(json)
    const [newProfileView, setNewProfileView] = useState(null)

    return <>
        <strong>{profiles.length > 0 ? HAS_PROFILES_MSG : NO_PROFILES_MSG}</strong>
        <p />
        <ListGroup>
            {profiles.map((profile, idx) => <Profile
                key={idx}
                profile={profile}
                disabledIntervals={sumDisabledIntervals()}
                onProfileDeletion={onProfileDeletion.bind(profile)}
            />)}
            {newProfileView}
        </ListGroup>
        <p />
        {newProfileView != null ?
            <> </> :
            <Button
                variant="info"
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

    //---------
    //
    function onProfileCreation({ profile }) {
        if (profile) updateProfiles([...profiles, profile])
        setNewProfileView(null)
    }

    //Where 'this' is binded profile
    function onProfileDeletion() {
        updateProfiles(profiles.filter(profile => profile.name != this.name))
    }

    function sumDisabledIntervals() {
        function reducer(intervals, profile) {
            return [...intervals, {
                start: getTodayAtSpecificHour(profile.startTime.match(/.+?(?=:)/)[0]),
                end: getTodayAtSpecificHour(profile.endTime.match(/.+?(?=:)/)[0])
            }]
        }
        console.log(profiles)
        return profiles.reduce(reducer, [])
    }
}