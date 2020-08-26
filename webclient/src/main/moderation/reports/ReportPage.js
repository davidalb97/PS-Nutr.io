import React, { useState } from 'react'
import useFetch, { FetchState } from '../../common/useFetch'
import RequestingEntity from '../../common/RequestingEntity'


import Card from 'react-bootstrap/Card'
import Tabs from 'react-bootstrap/Tabs'
import Tab from 'react-bootstrap/Tab'
import ListGroup from 'react-bootstrap/ListGroup'

import Loading from '../../bootstrap-common/Loading'
import Report from './Report'

export default function ReportPage() {
    const [request, setRequest] = useState({})
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
                        <ListGroup variant="flush">
                            <Reports url="http://localhost:9000/api/report?type=RESTAURANT" />
                        </ListGroup>
                    </Card.Body>
                </Tab>

                <Tab eventKey="meals" title="Meals">
                    <Card.Body>
                        <ListGroup variant="flush">
                            <Reports url="http://localhost:9000/api/report?type=RESTAURANT_MEAL" />
                        </ListGroup>
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
        onError={}
        onSuccess={displayReports}
    />

    function displayReports({ json }) {
        return <ListGroup variant="flush">
            {json.reports.map((report, idx) => <>
                <ListGroup.Item>
                    <Report key={idx} report={report} />
                </ListGroup.Item>
            </>)}
        </ListGroup>
    }
}