import React from 'react'

const AUTH_TOKEN_KEY = "AUTH_TOKEN"

const contextDefaultValue = {
    authToken: undefined,
    user: {
        email: undefined,
        username: undefined,
        image: undefined
    },
    notInitialized: true,
}

const UserContext = React.createContext(contextDefaultValue)
export default UserContext
