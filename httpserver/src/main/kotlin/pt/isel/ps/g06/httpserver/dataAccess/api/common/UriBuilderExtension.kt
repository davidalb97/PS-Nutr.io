package pt.isel.ps.g06.httpserver.dataAccess.api.common

import org.springframework.web.util.UriComponentsBuilder

/**
 * Extension method added because according to the documentation, [UriComponentsBuilder.queryParam]
 * adds query string under the format **"http://example.com?key"** when value is null.
 *
 * While the [URI RFC](https://tools.ietf.org/html/rfc3986#section-3.4) does not mandate a key-pair format,
 * some APIs - like [Spoonacular](https://spoonacular.com/food-api) - are not compatible with empty query values, hence
 * the need for this extension method.
 */
fun UriComponentsBuilder.nonNullQueryParam(query: String, value: Any?): UriComponentsBuilder {
    if (value == null) return this
    return this.queryParam(query, value)
}