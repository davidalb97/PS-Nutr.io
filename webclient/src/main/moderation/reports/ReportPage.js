import React, { useState } from 'react'
import RequestingEntity from '../../common/RequestingEntity'


import Card from 'react-bootstrap/Card'
import Tabs from 'react-bootstrap/Tabs'
import Tab from 'react-bootstrap/Tab'
import Alert from 'react-bootstrap/Alert'
import ListGroup from 'react-bootstrap/ListGroup'

import Loading from '../../bootstrap-common/Loading'
import Report from './Report'

export default function ReportPage() {
    const [tabKey, setTabKey] = useState('restaurants')

    return <Card>
        <Card.Header as="h1">Reports</Card.Header>
        <Card.Body>
            View all reports for restaurants or meals and act on them by banning the submitter
            or deleting said submission.
            <hr />
            <Tabs
                mountOnEnter
                activeKey={tabKey}
                onSelect={setTabKey}
            >
                <Tab className="tab-item-wrapper" eventKey="restaurants" title="Restaurants">
                    <Card.Body>
                        <Reports url="http://localhost:8080/report?type=RESTAURANT" />
                    </Card.Body>
                </Tab>

                <Tab eventKey="meals" title="Meals">
                    <Card.Body>
                        <Reports url="http://localhost:8080/report?type=RESTAURANT_MEAL" />
                    </Card.Body>
                </Tab>
            </Tabs>
        </Card.Body>
    </Card >
}

function Reports({ url }) {
    return <RequestingEntity
        request={{ url: url }}
        onDefault={Loading}
        onSuccess={displayResult}
    />

    function displayResult({ json }) {
        if (json && json.reportedSubmissions.length <= 0) {
            return <>There are currently no reports.</>
        }

        const items = json.reportedSubmissions.map((report, idx) => {
            return <ListGroup.Item key={idx}> <Report report={report} /> </ListGroup.Item>
        })

        return <ListGroup variant="flush" >{items}</ListGroup >
    }
}