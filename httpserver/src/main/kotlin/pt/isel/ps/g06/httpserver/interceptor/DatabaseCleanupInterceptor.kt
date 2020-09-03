package pt.isel.ps.g06.httpserver.interceptor

import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import pt.isel.ps.g06.httpserver.dataAccess.db.common.DatabaseContext
import java.lang.Exception
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val log = Logger.getLogger(DatabaseCleanupInterceptor::class.simpleName)

/**
 * Responsible for closing database handle connections, after all lazily acquired values (i.e: ResultIterable<T>)
 * were iterated throughout the request.
 *
 * From [JDBI's documentation](https://jdbi.org/#_on_demand) regarding streams:
 * **_"Returning cursor-like types such as Stream<T> or Iterable<T> outside of the outermost on-demand call does not work.
 * Since the Handle is closed, the database cursor is released and reading will fail."_**
 */
@Component
class DatabaseCleanupInterceptor(private val databaseContext: DatabaseContext) : HandlerInterceptor {

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        super.afterCompletion(request, response, handler, ex)
        if (databaseContext.isHandleOpen()) {
            databaseContext.close()
        }
    }
}