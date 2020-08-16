import React from 'react'

export default function ({ onLogin }) {
    const [credentials, setCredentials] = useState({ username: "", password: "" })


    async function handleSubmit(event) {
        event.preventDefault()

        if (isInputValid()) {
            onLogin(credentials.username, credentials.password)
        }
        else {
            console.log("Some error here!")
            return Promise.resolve()
        }
    }


    return <>
        <form onSubmit={handleSubmit}>
            <input type='text' name='username' placeholder='Your username' onChange={handleInputChange} />
            <input type='password' name='password' placeholder='Your password' onChange={handleInputChange} />
        </form>
    </>
}