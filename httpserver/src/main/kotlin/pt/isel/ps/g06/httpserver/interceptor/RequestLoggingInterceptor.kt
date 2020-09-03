package pt.isel.ps.g06.httpserver.interceptor

import org.slf4j.LoggerFactory
import org.springframework.http.server.ServletServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.UriComponentsBuilder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val log = LoggerFactory.getLogger(LoggerInterceptor::class.java)

/**
 * Intercepts outbound server responses and logs them.
 */
@Component
class LoggerInterceptor : HandlerInterceptor {


    override fun afterCompletion(
            request: HttpServletRequest,
            response: HttpServletResponse,
            handler: Any,
            ex: Exception?
    ) {
        super.afterCompletion(request, response, handler, ex)

        val uri = UriComponentsBuilder.fromHttpRequest(ServletServerHttpRequest(request)).build().toUriString()
        val logMsg = "${request.method} '$uri' status: ${response.status}"

        if (ex != null) log.error(logMsg, ex)
        else log.info(logMsg)
    }
}