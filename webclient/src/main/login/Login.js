
import React, { useState, useCallback, useEffect } from 'react'
import { Switch, Route, Redirect, Link } from "react-router-dom"

import UserContext from '../authentication/UserContext'

import LoginPage from './LoginPage'
import RegisterPage from './RegisterPage'
import RequestingEntity from '../common/RequestingEntity'


const AUTH_TOKEN_KEY = "AUTH_TOKEN"

export default function Login({ children }) {
    const [loginContext, setLoginContext] = useState({
        request: { url: "" },
        authToken: sessionStorage.getItem(AUTH_TOKEN_KEY),
        credentials: undefined,
        get isLoggedIn() { return this.authToken }
    })

    const handleAuthentication = function () {
        setLoginContext({
            ...loginContext,
            request: { url: this.url, method: "POST", body: arguments[0] }
        })
    }

    function authenticateUser({ json }) {
        if (!json.jwt) {
            //TODO
            return null
        }

        sessionStorage.setItem(AUTH_TOKEN_KEY, json.jwt)
        setLoginContext({ ...loginContext, authToken: json.jwt })

        return <> </>
    }

    if (!loginContext.isLoggedIn) {
        return <>
            {displayAuthenticationProviders(handleAuthentication)}

            <RequestingEntity
                request={loginContext.request}
                onSuccess={authenticateUser}
            />
        </>
    }

    return <UserContext.Provider value={{ authToken: `Bearer ${loginContext.authToken}`, }}>
        {children}
    </UserContext.Provider>
}


function displayAuthenticationProviders(handleAuthentication) {
    return <>
        <div className={`topnav`}>
            <Link to="/login"><button >Login</button></Link>
            <Link to="/register"><button >Register</button></Link>
        </div>

        < Switch >
            <Route path="/login">
                <LoginPage
                    onLogin={handleAuthentication.bind({ url: "http://localhost:9000/api/user/login" })}
                />
            </Route>

            <Route path="/register">
                <RegisterPage
                    onRegister={handleAuthentication.bind({ url: "http://localhost:9000/api/user/register" })}
                />
            </Route>
        </Switch >
    </>
}