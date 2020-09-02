package pt.isel.ps.g06.httpserver.interceptor

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
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

        if(ex != null) {
            log.error("${request.method} '${request.requestURI}' status: ${response.status}", ex)
        } else log.info("${request.method} '${request.requestURI}' status: ${response.status}")
    }
}