import React from 'react'

const AUTH_TOKEN_KEY = "AUTH_TOKEN"

const contextDefaultValue = {
    authToken: undefined,
    user: undefined,
    initialized: false,
}

const UserContext = React.createContext(contextDefaultValue)
export default UserContext
