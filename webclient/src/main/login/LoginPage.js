import React, { useState } from 'react'

export default function LoginPage({ onLogin }) {
    const [credentials, setCredentials] = useState({ email: "", password: "" })


    function handleInputChange(event) {
        const newCredentials = { ...credentials }
        newCredentials[event.target.name] = event.target.value
        setCredentials(newCredentials)
    }


    async function handleSubmit(event) {
        event.preventDefault()

        if (isInputValid()) {
            onLogin(credentials.email, credentials.password)
        }
        else {
            console.log("Some error here!")
            return Promise.resolve()
        }
    }

    function isInputValid() {
        function isNullOrBlank(str) {
            return !str || str.trim().length === 0
        }
        return !isNullOrBlank(credentials.email) && !isNullOrBlank(credentials.password)
    }

    return <>
        <form onSubmit={handleSubmit}>
            <input type='email' name='email' placeholder='Your email' onChange={handleInputChange} />
            <input type='password' name='password' placeholder='Your password' onChange={handleInputChange} />
            <button type="submit">Login</button>
        </form>
    </>
}