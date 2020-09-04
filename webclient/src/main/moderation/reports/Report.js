import React, { useState } from 'react'
import { Link } from 'react-router-dom'
import RequestingEntity from '../../common/RequestingEntity'

import Button from 'react-bootstrap/Button'
import ButtonGroup from 'react-bootstrap/ButtonGroup'
import ListGroup from 'react-bootstrap/ListGroup'
import Alert from 'react-bootstrap/Alert'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'
import Loading from '../../bootstrap-common/Loading'
class ReportActions {
    constructor(report, triggerRequest) {
        this.report = report
        this.triggerRequest = triggerRequest
    }
    BAN() {
        const request = {
            url: `http://localhost:9000/api/user`,
            method: `PUT`,
            body: { submitterId: this.report.submissionDetail.submitterId, isBanned: true }
        }

        this.triggerRequest(request)
    }
    DISMISS() {
        const request = {
            url: `http://localhost:9000/api/report/${this.reportId}`,
            method: `DELETE`
        }

        this.triggerRequest(request)
    }
    REMOVE() {
        const request = {
            url: `http://localhost:9000/api/submission/${this.report.submissionDetail.submissionId}`,
            method: `DELETE`
        }

        this.triggerRequest(request)
    }
}

export default function Report({ report }) {
    const [request, setRequest] = useState({})

    const hasInfoButton = report.reports
    const submissionDetail = report.submissionDetail
    const actions = new ReportActions(report, setRequest)

    return <RequestingEntity
        request={request}
        onLoad={Loading}
        onDefault={DisplayReport}
        onSuccess={() => <Alert variant="success">Success! This report will disappear shortly..</Alert>}
    />

    function DisplayReport() {
        return <>
            <Row>
                {/* Submission detail */}
                <Col >
                    <strong>Name:</strong> {submissionDetail.submissionName}<p />
                    <p><strong>Reports:</strong> {report.reportCount || report.reports.length}</p>
                </Col>

                <Col xs sm md="auto">
                    <ButtonGroup aria-label="Action buttons">
                        {hasInfoButton ? undefined :
                            <Link to={`/moderation/reports/${submissionDetail.submissionId}`}>
                                <Button variant="outline-info">Info</Button>
                            </Link>
                        }

                        <Button
                            variant="outline-danger"
                            onClick={actions.REMOVE.bind(actions)}
                        >
                            Remove
                        </Button>

                        <Button
                            variant="outline-danger"
                            onClick={actions.BAN.bind(actions)}
                        >
                            Ban
                        </Button>
                    </ButtonGroup>
                </Col>
            </Row>
            <SubmissionReports />
        </>
    }


    function SubmissionReports() {
        if (!report.reports) return <> </>

        return <>
            <hr />
            <strong>All reports</strong>
            <ListGroup variant="flush">
                {report.reports.map((reportInfo, idx) => <ListGroup.Item key={idx}>
                    <Row>
                        <Col>
                            {reportInfo.text}
                        </Col>
                        <Col className="d-flex justify-content-end">
                            <Button
                                variant="outline-danger"
                                onClick={actions.DISMISS.bind({ ...actions, reportId: reportInfo.reportId })}
                            >
                                Dismiss
                        </Button>
                        </Col>
                    </Row>
                </ListGroup.Item>
                )}
            </ListGroup>
        </>
    }
}
