
import React, { useState, useCallback } from 'react'
import { Switch, Route, Redirect, Link } from "react-router-dom"

import AuthenticationService from './AuthenticationService'

import LoginPage from './LoginPage'
import RegisterPage from './RegisterPage'

export default function Login({ children }) {
    const [authenticationService] = useState(AuthenticationService())
    const [isLoggedIn, setLoggedIn] = useState(authenticationService.isLoggedIn())

    const handleLogin = useCallback((username, password) => {
        if (authenticationService.login(username, password)) {
            setLoggedIn(true)
        }
    }, [authenticationService, setLoggedIn])

    const handleRegister = useCallback(() => {

    }, [authenticationService])


    const handleLogout = useCallback(() => {
        authenticationService.logout()
        setLoggedIn(false)
    }, [authenticationService, setLoggedIn])


    return (
        <Switch>
            <Route exact path="/login">
                {!isLoggedIn ? <LoginPage onLogin={handleLogin} /> : <Redirect to="/" />}
            </Route>

            <Route exact path="/register">
                {!isLoggedIn ? <RegisterPage onRegister={handleRegister} /> : <Redirect to="/" />}
            </Route>

            <Route>
                {!isLoggedIn ?
                    <div className={`topnav`}>
                        <Link to="/register"><button>Register</button> </Link>
                        <br />
                        <Link to="/login"><button>Login</button></Link>
                    </div> :

                    <>
                        <div className={`topnav`}>
                            <button onClick={handleLogout}>Logout</button>
                        </div>
                        {children}
                    </>
                }
            </Route>
        </Switch >
    )
}
