
import React, { useState, useCallback, useEffect } from 'react'
import { Switch, Route, Redirect, Link } from "react-router-dom"

import AuthenticationService from './AuthenticationService'

import UserContext from '../authentication/UserContext'
import LoginPage from './LoginPage'
import RegisterPage from './RegisterPage'

export default function Login({ children }) {
    const [authenticationService] = useState(AuthenticationService())
    const [isLoggedIn, setLoggedIn] = useState(authenticationService.isLoggedIn())

    const handleLogin = useCallback((email, password) => {
        if (authenticationService.login(email, password)) {
            setLoggedIn(true)
        }
    }, [setLoggedIn])

    const handleRegister = useCallback((credentials) => {
        if (authenticationService.register(credentials)) {
            setLoggedIn(true)
        }
    }, [])


    const handleLogout = useCallback(() => {
        authenticationService.logout()
        setLoggedIn(false)
    }, [setLoggedIn])


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

                    <UserContext.Provider value={{ username: "Pedro", email: "pedro@email.com" }}>
                        <div className={`topnav`}>
                            <button onClick={handleLogout}>Logout</button>
                        </div>
                        {children}
                    </UserContext.Provider>
                }
            </Route>
        </Switch >
    )
}
