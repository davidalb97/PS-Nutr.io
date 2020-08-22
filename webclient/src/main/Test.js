import React, { useState, useRef } from 'react'


import Form from 'react-bootstrap/Form'

export default function Test() {
    const [value, setValue] = useState("Initial value")
    const ref = useRef()

    return <Form>
        <Form.Group controlId="name">
            <Form.Label>Name (optional)</Form.Label>
            <Form.Control
                value={value}
                onChange={() => onInput(ref.current)}
                ref={ref}
                autoComplete="off"
                type="text"
            />
        </Form.Group>
    </Form>

    function onInput(a) {
        setValue(a.value)
    }
}


/*
return <Card>
        <Card.Header as="h1">Insulin Profile</Card.Header>
        <Card.Body>
            An insulin profile allows you to use our insulin calculator in order to automatically calculate the amount of insulin units needed
            when eating meals at a restaurant.
            <hr />
        <Selector/>
        </Card.Body>
    </Card>


    //--------------
    //Functions
    function Selector() {
        switch (fetchState) {
            case FetchStates.fetching: return displayLoading()
            case FetchStates.error: return displayError()
            case FetchStates.done: return displayInsulinProfiles()
            default: return null
        }
    }

    function displayInsulinProfiles() {
        return <>
            <strong>{profiles.length > 0 ? HAS_PROFILES_MSG : NO_PROFILES_MSG}</strong>
            <p/>
            <ListGroup>
                {profiles.map((profile, idx) => <Profile 
                    key={idx}
                    profile={profile}
                    updateProfile={(profile) => update({profile: profile, identifier: idx})}
                />
            )}
            </ListGroup>
            <p/>
            <Button variant="outline-primary" 
                onClick={() => update({profile: emptyProfile, identifier: profiles.length})} 
                block
            >
                Add profile
            </Button>
        </>
    }

    function displayError() {
        return <Alert variant="danger">
            <Alert.Heading>Failed to obtain your insulin profiles!</Alert.Heading>
            <p className="mb-0">
                We currently can't access your insulin profiles. Maybe try again later?
            </p>
        </Alert>
    }

    function displayLoading() {
        return <Spinner animation="border" role="status">
            <span className="sr-only">Loading...</span>
        </Spinner>
    }

    function update({profile, identifier}) {
        const newProfiles = [...profiles]
        newProfiles[identifier] = profile
        updateProfiles(newProfiles)
        console.log(newProfiles)
    }


function Profile({ profile, updateProfile }) {
        const header = profile.isNewProfile ?
            <NewProfileHeader profile={profile} onProfileChange={onProfileChange} /> :
            <ProfileHeader />
    
        return <ListGroup.Item>
            <Form>
                {header}
                <TimeRange
                    error={false}
                    ticksNumber={18}
                    timelineInterval={[startOfToday(), endOfToday()]}
                    onUpdateCallback={onUpdateCallback}
                    onChangeCallback={onChangeCallback}
                />
            </Form>
        </ListGroup.Item>
    
        function onUpdateCallback({ error }) {
            // console.log(error)
        }
    
        function onChangeCallback(selectedInterval) {
            // console.log(selectedInterval)
        }
    
        function onProfileChange(key, value) {
            const updatedProfile = { ...profile }
            updatedProfile[key] = value
            updateProfile(updatedProfile)
        }
    
        function NewProfileHeader({ profile, onProfileChange }) {
            //TODO Add "?" for more info on mouse hover
            return <>
                <Form.Group controlId="name">
                    <Form.Label>Name (optional)</Form.Label>
                    <Form.Control
                        value={profile.name}
                        onChange={() => onInput(name.current)}
                        ref={name}
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
                        value={profile.glucoseObjective}
                    />
                </Form.Group>
    
                <Form.Group controlId="carbohydrateRatio">
                    <Form.Label>Carbohydrate ratio</Form.Label>
                    <Form.Control
                        onChange={() => onInput(carbRatio.current)}
                        ref={carbRatio}
                        type="number"
                        value={profile.carbohydrateRatio}
                    />
                </Form.Group>
    
                <Form.Group controlId="sensitivityFactor">
                    <Form.Label>Sensitivity factor</Form.Label>
                    <Form.Control
                        onChange={() => onInput(sensitivityFactor.current)}
                        ref={sensitivityFactor}
                        type="number"
                        value={profile.sensitivityFactor}
                    />
                </Form.Group>
            </>
    
            function onInput(ref) {
                onProfileChange(ref.id, ref.value)
            }
        }
    
        function ProfileHeader() {
            return <>
                <p>Name (optional)</p>
                <p>Glucose objective</p>
                <p>Carbohydrate ratio</p>
                <p>Sensitivity factor</p>
            </>
        }
    }
*/