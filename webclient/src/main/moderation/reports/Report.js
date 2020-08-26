import React, { useReducer } from 'react'
import { Redirect } from 'react-router'
import useFetch, { FetchStates } from '../../common/useFetch'
import RequestingEntity from '../../common/RequestingEntity'


import ListGroup from 'react-bootstrap/ListGroup'
import Button from 'react-bootstrap/Button'
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Alert from 'react-bootstrap/Alert'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Loading from '../../bootstrap-common/Loading'

export default function Report({ report }) {
    const [request, setRequest] = useReducer((req, url) => { return { url: url, method: "DELETE" } }, {})
    const [redirect, setRedirect] = useReducer(_ => true, false)
    const isdetailed = report.reports


    return < RequestingEntity
        request={request}
        onLoad={Loading}
        onDefault={DisplayReport}
        onError={Error}
        onSuccess={Success}
    />


    function Success() {
        return <Alert variant="success">Success! This report will disappear shortly..</Alert>
    }

    function Error() {
        return <Alert variant="danger">Unable to fulfil your request! Please try again later.</Alert>
    }

    function DisplayReport() {
        return <>
            {redirect ? <Redirect to={`/moderation/reports/${1}`} /> : undefined}
            <Row>
                {/* Submission detail */}
                <Col >
                    <strong>Name:</strong> Some submission <p />
                    <strong>Reports:</strong> 10
            </Col>

                {/* Loading spinner */}
                {fetchState !== FetchStates.loading ? undefined : <Col> <Loading /> </Col>}

                <Col xs sm md="auto">
                    <ButtonGroup aria-label="Action buttons">
                        {!isDetailed ? undefined :
                            <Button
                                variant="outline-info"
                                onClick={setRedirect}
                                disabled={fetchState === FetchStates.loading || fetchState === FetchStates.error}
                            >
                                Info
                    </Button>}

                        <Button
                            variant="outline-danger"
                            onClick={() => setRequest("http://localhost:9000/api")}
                            disabled={fetchState === FetchStates.loading || fetchState === FetchStates.error}
                        >
                            Remove
                    </Button>

                        <Button
                            variant="outline-danger"
                            onClick={() => setRequest("http://localhost:9000/api")}
                            disabled={fetchState === FetchStates.loading || fetchState === FetchStates.error}
                        >
                            Ban
                    </Button>

                        <Button
                            variant="outline-danger"
                            disabled={fetchState === FetchStates.loading || fetchState === FetchStates.error}
                        >
                            Dismiss
                    </Button>
                    </ButtonGroup>
                </Col>
            </Row>
        </>
    }

}