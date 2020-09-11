package pt.isel.ps.g06.httpserver.extensions

import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import pt.isel.ps.g06.httpserver.dataAccess.db.ApiSubmitterMapper

const val DEFAULT_SCOPE = RequestAttributes.SCOPE_REQUEST

fun <T : Any> getOrPut(key: String, valueSupplier: () -> T, callback: ((T) -> Unit)? = null): T {
    val requestAttributes = RequestContextHolder
            .getRequestAttributes()
            ?: CustomAttributes().also { RequestContextHolder.setRequestAttributes(it, true) }

    @Suppress("UNCHECKED_CAST")
    var value = requestAttributes.getAttribute(key, DEFAULT_SCOPE) as? T

    if (value == null) {
        println("Opening a new Handle!")
        value = valueSupplier()
        requestAttributes.setAttribute(key, value, DEFAULT_SCOPE)
    }

    if (callback != null) requestAttributes.registerDestructionCallback(key, { callback.invoke(value) }, DEFAULT_SCOPE)
    return value
}


/**
 * Custom [RequestAttributes] implementation for when [RequestContextHolder] is called outside a HTTP request scope.
 * This is a very specific case and it only happens due to [ApiSubmitterMapper.createMap].
 *
 * For simplicity, this implementation only allows for [RequestAttributes.SCOPE_REQUEST] scope
 * and only the required methods are implemented
 */
class CustomAttributes : RequestAttributes {
    private val attributeMap: MutableMap<String, Any> = mutableMapOf()

    override fun getAttribute(name: String, scope: Int): Any? {
        return if (scope == DEFAULT_SCOPE) attributeMap[name]
        else null
    }

    override fun setAttribute(name: String, value: Any, scope: Int) {
        if (scope == DEFAULT_SCOPE) attributeMap[name] = value
    }

    override fun removeAttribute(name: String, scope: Int) {
        if (scope == DEFAULT_SCOPE) attributeMap.remove(name)
    }

    override fun getAttributeNames(scope: Int): Array<String> {
        return if (scope == DEFAULT_SCOPE) attributeMap.keys.toTypedArray()
        else emptyArray()
    }

    override fun registerDestructionCallback(name: String, callback: Runnable, scope: Int) {
        //Can't throw 'not yet implemented' because it is used above.
        //Instead, have 'DatabaseContext.close()' close it explicitly.
    }

    override fun resolveReference(key: String): Any? {
        throw NotImplementedError()
    }

    override fun getSessionId(): String {
        throw NotImplementedError()
    }

    override fun getSessionMutex(): Any {
        throw NotImplementedError()
    }
}