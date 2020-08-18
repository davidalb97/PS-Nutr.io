
import React, { useState, useCallback, useEffect } from 'react'
import { Switch, Route, Redirect, Link } from "react-router-dom"

import useFetch, { FetchStates } from '../common/useFetch'
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

    function onAuthentication({ json }) {
        if (!json.jwt) return //onError

        sessionStorage.setItem(AUTH_TOKEN_KEY, json.jwt)
        setLoginContext({ ...loginContext, authToken: json.jwt })
    }

    if (!loginContext.isLoggedIn) {
        return <>
            {displayAuthenticationProviders(handleAuthentication)}

            <RequestingEntity
                request={loginContext.request}
                onSuccess={onAuthentication}
            />
        </>
    }

    //TODO Get user information
    return <UserContext.Provider value={{ authToken: loginContext.authToken, }}>
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


 // if (fetchState == FetchStates.done) {
    //     console.log(json)
    //     return <UserContext.Provider value={{ ...loginContext.credentials, authToken: json }} >
    //         <div className={`topnav`}><button onClick={handleLogout}>Logout</button></div>
    //         {children}
    //     </UserContext.Provider>
    // }