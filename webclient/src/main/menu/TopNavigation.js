import React, { useState, useEffect, useContext } from 'react'
import { Link } from 'react-router-dom'

import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'
import Button from 'react-bootstrap/Button'

import UserContext from '../authentication/UserContext'
export default function TopNavigation() {
    const [init, setInit] = useState(false)
    const user = useContext(UserContext)

    useEffect(() => { if (!init) setInit(true) }, [init])


    if (!init) return <> </>

    //Props for navbar
    const disabled = user.notInitialized
    const authenticationContext = user.notInitialized ?
        <> <Button variant="primary">Login</Button> <br /> <Button variant="primary">Register</Button></> :
        <> <Navbar.Text>Signed in as: {`${user.username}`}</Navbar.Text> </>

    return <Navbar bg="dark" variant="dark" expand="lg" fixed="top">
        <Navbar.Brand><Link to="/" />Nutr.io</Navbar.Brand>
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
                    <NavDropdown.Item><Link to="/moderation/addFood">Food creator</Link></NavDropdown.Item>
                </NavDropdown>

                {authenticationContext}
            </Nav>
        </Navbar.Collapse>
    </Navbar >
}