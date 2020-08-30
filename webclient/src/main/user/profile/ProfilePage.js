import React, { useContext, useState, useReducer } from 'react'
import { Link, Redirect } from 'react-router-dom'
import useFetch, { FetchStates } from '../../common/useFetch'
import { deleteCookie } from '../../authentication/CookieService'

import Card from 'react-bootstrap/Card'
import Figure from 'react-bootstrap/Figure'
import Button from 'react-bootstrap/Button'
import Modal from 'react-bootstrap/Modal'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import FetchError from '../../bootstrap-common/FetchError'
import SuccessAlert from '../../bootstrap-common/SuccessAlert'
import Loading from '../../bootstrap-common/Loading'
import UserContext from '../../authentication/UserContext'

export default function ProfilePage() {
    const context = useContext(UserContext)
    const [showModal, setShowModal] = useState(false)
    const [redirect, setRedirect] = useState(false)

    if (redirect) return <Redirect to="/" />

    return <Card>
        <Card.Header as="h1">Your profile</Card.Header>
        <Card.Body>
            <Body />
        </Card.Body>
    </Card>

    function Body() {
        if (!context.initialized) return <Loading />

        if (!context.user) return (
            <Row>
                You are not logged in!
                Either <Link to="/login">log in</Link>
                or <Link to="/register">register</Link> if you don't have an account.
            </Row>
        )

        return <>
            <Row>
                <Col>
                    <strong>Username:</strong> {context.user.username} <p />
                    <strong>Email:</strong> {context.user.email}
                </Col>
                <Col xs sm md="auto">
                    <Figure>
                        <Figure.Image
                            width={512}
                            height={512}
                            alt="Profile image"
                            src={context.user.userImage}
                        />
                    </Figure>
                </Col>
            </Row>
            <div className="spacer-20" />
            <Row className="justify-content-center">
                <Button
                    variant="danger"
                    className="profile-delete-cta"
                    block
                    onClick={() => setShowModal(true)}
                >
                    Delete profile
                </Button>
            </Row>

            <ConfirmationModal
                show={showModal}
                onClose={success => { setShowModal(success); setRedirect(success) }}
            />
        </>
    }
}

function ConfirmationModal({ show, onClose }) {
    const [request, triggerRequest] = useReducer(() => {
        return {
            url: "http://localhost:9000/api/user/1",
            method: "DELETE",
        }
    }, {})

    const [fetchState, response, json, error] = useFetch(request)

    let body
    switch (fetchState) {
        case FetchStates.fetching: body = <Loading />; break
        case FetchStates.error: body = <FetchError error={error} json={json} />; break
        case FetchStates.done: {
            deleteCookie()
            body = <SuccessAlert
                heading="Deleted account with success!"
                body="Thank you for being part of our journey."
            />;
            break;
        }

        default: body = <span>
            Deleting your account will permanently remove all your data, including your
            favorites, submissions and insulin profiles!
            <p />
            <strong>This operation is irreversible!</strong>
        </span>
    }

    return <>
        <Modal show={show} onHide={handleClose} centered size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Are you sure?</Modal.Title>
            </Modal.Header>

            <Modal.Body>{body}</Modal.Body>

            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose} disabled={fetchState === FetchStates.fetching}>
                    Close
                </Button>
                <Button
                    variant="danger"
                    onClick={triggerRequest}
                    disabled={fetchState === FetchStates.fetching || fetchState === FetchStates.done}
                >
                    Delete
                </Button>
            </Modal.Footer>
        </Modal>
    </>

    function handleClose() {
        switch (fetchState) {
            case FetchStates.done: return onClose(true)

            //Block modal closure until request is done
            case FetchStates.fetching: return

            default: return onClose(false)
        }
    }
}