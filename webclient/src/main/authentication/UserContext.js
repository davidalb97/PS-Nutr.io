import React from 'react'

const contextDefaultValue = {
    authToken: undefined,
    user: {
        email: undefined,
        username: undefined,
        image: undefined
    },
}

const UserContext = React.createContext(contextDefaultValue)
export default UserContext
