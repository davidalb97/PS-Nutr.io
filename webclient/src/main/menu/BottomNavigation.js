import React, { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'

import Navbar from 'react-bootstrap/Navbar'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

export default function TopNavigation() {
    const [init, setInit] = useState(false)

    useEffect(() => { if (!init) setInit(true) }, [init, setInit])

    if (!init) return <> </>

    return <Row>
        <Col>
            <Navbar bg="dark" variant="dark" expand="lg" fixed="bottom">
                <Navbar.Toggle />
                <Navbar.Collapse className="justify-content-center">
                    <Navbar.Text>Are you a restaurant owner? <Link to="/contactUs">Contact us</Link></Navbar.Text>
                </Navbar.Collapse>
            </Navbar >
        </Col>
    </Row>
}