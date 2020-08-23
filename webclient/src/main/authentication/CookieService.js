const AUTH_TOKEN_KEY = "AUTH_TOKEN"

export function get(name) {
    const cookieName = name || AUTH_TOKEN_KEY

    var match = document.cookie.match(new RegExp('(^| )' + cookieName + '=([^;]+)'));
    return match ? match[2] : undefined
}

export function set(token) {

}