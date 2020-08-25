import React, { useState, useEffect, useContext } from 'react'
import { Link } from 'react-router-dom'

import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'
import Button from 'react-bootstrap/Button'
import Row from 'react-bootstrap/Row'
import Col from 'react-bootstrap/Col'

import { LinkContainer } from 'react-router-bootstrap'

import UserContext from '../authentication/UserContext'
export default function TopNavigation() {
    const [init, setInit] = useState(false)
    const userContext = useContext(UserContext)

    useEffect(() => { if (!init) setInit(true) }, [init])

    if (!init) return <> </>

    const disabled = !userContext.initialized || (userContext.initialized && !userContext.user)
    //If user was still not obtained from server, do not display Login/Register buttons
    const authenticationContext = !userContext.initialized ? <> </> : userContext.user ?
        //Logged in, display info
        <Navbar.Text>Signed in as: {`${userContext.user.username}`}</Navbar.Text> :
        //----
        //Not signed in, but tried to obtain from server
        <>
            <LinkContainer to="/login"><Button variant="primary">Login</Button></LinkContainer>
            <LinkContainer to="/register"><Button variant="primary">Register</Button></LinkContainer>
        </>

    return <Navbar bg="dark" variant="dark" expand="lg" fixed="top">
        <Navbar.Brand><Link to="/" >Nutr.io</Link></Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
                {/* MEALS */}
                <NavDropdown title="Meal" id="collasible-nav-dropdown" disabled={disabled}>
                    <NavDropdown.Item><Link to="/meals">View custom meals</Link></NavDropdown.Item>
                    <NavDropdown.Item><Link to="/meals/create">Add custom meal</Link></NavDropdown.Item>
                </NavDropdown>
                {/* USER */}
                <NavDropdown title="User" id="collasible-nav-dropdown" disabled={disabled}>
                    <NavDropdown.Item><Link to="/user">View profile</Link></NavDropdown.Item>
                    <NavDropdown.Item><Link to="/user/insulin">View insulin profiles</Link></NavDropdown.Item>
                </NavDropdown>
                {/* MODERATION */}
                <NavDropdown title="Moderation" id="collasible-nav-dropdown" disabled={disabled}>
                    <NavDropdown.Item><Link to="/moderation/newFood">Food creator</Link></NavDropdown.Item>
                    <NavDropdown.Item><Link to="/moderation/reports">Reports</Link></NavDropdown.Item>
                </NavDropdown>

                {authenticationContext}
            </Nav>
        </Navbar.Collapse>
    </Navbar >
}