import React, { useState, useEffect, useContext } from 'react'
import { Link } from 'react-router-dom'

import Navbar from 'react-bootstrap/Navbar'
import Nav from 'react-bootstrap/Nav'
import NavDropdown from 'react-bootstrap/NavDropdown'
import Button from 'react-bootstrap/Button'

import { LinkContainer } from 'react-router-bootstrap'

import UserContext from '../authentication/UserContext'
export default function TopNavigation() {
    const [init, setInit] = useState(false)
    const userContext = useContext(UserContext)

    useEffect(() => { if (!init) setInit(true) }, [init])

    if (!init) return <> </>

    const navigation = !userContext.initialized ? <> </> : userContext.user ?
        <AuthenticatedNavigation /> :
        <UnauthenticatedNavigation />

    return <Navbar bg="dark" variant="dark" expand="lg" fixed="top">
        <Navbar.Brand><Link to="/" >Nutr.io</Link></Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
            {navigation}
        </Navbar.Collapse>
    </Navbar >

    function AuthenticatedNavigation() {
        return <>
            <Nav className="d-flex justify-content-start top-navbar">
                {/* MEALS */}
                <NavDropdown title="Meal" id="collasible-nav-dropdown">
                    <NavDropdown.Item><Link to="/meals">View custom meals</Link></NavDropdown.Item>
                    <NavDropdown.Item><Link to="/meals/create">Add custom meal</Link></NavDropdown.Item>
                </NavDropdown>
                {/* USER */}
                <NavDropdown title="User" id="collasible-nav-dropdown">
                    <NavDropdown.Item><Link to="/user">View profile</Link></NavDropdown.Item>
                    <NavDropdown.Item><Link to="/user/insulin">View insulin profiles</Link></NavDropdown.Item>
                </NavDropdown>
                {/* MODERATION */}
                <NavDropdown title="Moderation" id="collasible-nav-dropdown">
                    <NavDropdown.Item><Link to="/moderation/newFood">Food creator</Link></NavDropdown.Item>
                    <NavDropdown.Item><Link to="/moderation/reports">Reports</Link></NavDropdown.Item>
                </NavDropdown>
            </Nav>

            <Nav className="d-flex justify-content-end top-navbar">
                <Navbar.Text>Signed in as: <Link to="/user">{`${userContext.user.username}`}</Link></Navbar.Text>
                <Button variant="primary" className="nav-register-btn" onClick={() => userContext.onLogout()}>Sign out</Button>
            </Nav>
        </>
    }

    function UnauthenticatedNavigation() {
        return <Nav className="d-flex justify-content-end top-navbar">
            <LinkContainer to="/login"><Button variant="outline-primary" active={false}>Login</Button></LinkContainer>
            <LinkContainer to="/register"><Button variant="primary" className="nav-register-btn">Register</Button></LinkContainer>
        </Nav>
    }
}