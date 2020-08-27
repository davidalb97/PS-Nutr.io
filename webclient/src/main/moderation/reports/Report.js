import React, { useReducer } from 'react'
import { Link } from 'react-router-dom'
import RequestingEntity from '../../common/RequestingEntity'

import Button from 'react-bootstrap/Button'
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import Alert from 'react-bootstrap/Alert'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Loading from '../../bootstrap-common/Loading'

const report = {
    submissionIdentifier: 1,
    reportIdentifier: 2,
    submitterIdentifier: 1,
    name: "Some report",
    reports: ["Bad language", "Wrong place"]
}

const ACTIONS = {
    BAN: submitter => {
        return {
            url: `http://localhost:9000/api/user`,
            method: `PUT`,
            body: { submitterId: submitter.submitterIdentifier, isBanned: true }
        }
    },
    DISMISS: submitter => {
        return {
            url: `http://localhost:9000/api/report/${submitter.reportIdentifier}`,
            method: `DELETE`
        }
    },
    REMOVE: submitter => {
        return {
            url: `http://localhost:9000/api/submission/${submitter.submissionIdentifier}`,
            method: `DELETE`
        }
    }
}

export default function Report({ }) {
    const [request, setRequest] = useReducer((req, action) => action(report), {})
    const hasInfoButton = report.reports

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
            <Row>
                {/* Submission detail */}
                <Col >
                    <strong>Name:</strong> {report.name}<p />
                    {!report.reportCount ? undefined : <p><strong>Reports:</strong> 10</p>}
                </Col>

                <Col xs sm md="auto">
                    <ButtonGroup aria-label="Action buttons">
                        {hasInfoButton ? undefined :
                            <Link to={`/moderation/reports/${report.submissionIdentifier}`}>
                                <Button variant="outline-info">  Info </Button>
                            </Link>
                        }

                        <Button
                            variant="outline-danger"
                            onClick={() => setRequest(ACTIONS.REMOVE)}
                        >
                            Remove
                        </Button>

                        <Button
                            variant="outline-danger"
                            onClick={() => setRequest(ACTIONS.BAN)}
                        >
                            Ban
                        </Button>

                        <Button
                            variant="outline-danger"
                            onClick={() => setRequest(ACTIONS.DISMISS)}
                        >
                            Dismiss
                        </Button>
                    </ButtonGroup>
                </Col>
            </Row>
        </>
    }
}
