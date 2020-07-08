package pt.isel.ps.g06.httpserver.interceptor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val log = LoggerFactory.getLogger(LoggerInterceptor::class.java)

@Component
class LoggerInterceptor : HandlerInterceptor {

    /**
     * Intercepts incoming user requests and logs them.
     */
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        log.info("Received http request: ${request.method} ${request.requestURI}")
        return true
    }
}