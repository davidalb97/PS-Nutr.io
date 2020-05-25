package pt.ipl.isel.leic.ps.androidclient.data.source

class UriBuilder {

    /**
     * At this time, it only replaces uri variables,
     * this class will have more features in the future
     */
    fun buildUri(
        baseUri: String,
        parameters: HashMap<String, String>?
    ): String {

        if (parameters.isNullOrEmpty()) {
            return baseUri
        }

        var uri = baseUri

        parameters.forEach { parameter ->
            uri = uri.replace(parameter.key, parameter.value)
        }

        return uri
    }
}