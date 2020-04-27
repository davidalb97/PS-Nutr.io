package pt.isel.ps.g06.httpserver.exception

private fun getMessage(invalidParams: HashMap<String, List<String>>): String {
    return invalidParams.toList().joinToString(", ") { getMessage(it.first, it.second) }
}

private fun getMessage(prefix: String, invalidParams: List<String>): String {
    return invalidParams.joinToString(", ", "$prefix = [", "]")
}

private fun getMessage(invalidParams: Map<String, String>): String {
    return invalidParams.toList().joinToString(", ") { "${it.first} = ${it.second}" }
}

class InvalidParameterException : Exception {

    constructor(invalidParams: HashMap<String, List<String>>) : super(getMessage(invalidParams))

    constructor(key: String, invalidParams: List<String>) : super(getMessage(key, invalidParams))

    constructor(invalidParams: Map<String, String>) : super(getMessage(invalidParams))

    constructor(key: String, value: String) : super(getMessage(key, listOf(value)))
}