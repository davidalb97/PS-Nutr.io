const AUTH_TOKEN_KEY = "AUTH_TOKEN"

export default function AuthenticationService() {
    console.log("Auth service called.")

    function getAuthorizationToken(username, password) {
        //TODO Get from HTTP Server
        let credentials = `${username}:${password}`
        return `Basic ${btoa(credentials)}`
    }

    return {
        /**
         * Method used to perform login with the given credentials.
         * 
         * @returns {Boolean}, true if login was successful, else false.
         */
        login: (email, password) => {
            sessionStorage.setItem(AUTH_TOKEN_KEY, getAuthorizationToken(email, password))
            return true
        },

        register: ({ username, email, password }) => {
            console.log(username)
            //TODO Register to servers
            return true
        },

        logout: () => {
            sessionStorage.removeItem(AUTH_TOKEN_KEY)
        },

        /**
         * Checks whether the user is logged in or not
         */
        isLoggedIn: () => {
            return sessionStorage.getItem(AUTH_TOKEN_KEY) != null
        },

        /**
         * @returns authorization token if the user is logged in, else null
         */
        getToken: () => {
            return sessionStorage.getItem(AUTH_TOKEN_KEY)
        }
    }
}