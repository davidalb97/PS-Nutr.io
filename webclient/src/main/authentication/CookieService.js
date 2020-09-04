const AUTH_TOKEN_KEY = "AUTH_TOKEN"

export function get(name = AUTH_TOKEN_KEY) {
    var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    return match ? match[2] : undefined
}

export function set({ name = AUTH_TOKEN_KEY, value }) {
    document.cookie = `${name}=${value}`
}

export function deleteCookie(name = AUTH_TOKEN_KEY) {
    set({ name: name, value: `; Max-Age=-99999;` })
}