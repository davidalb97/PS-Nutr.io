package pt.isel.ps.g06.httpserver.dataAccess.api

import org.springframework.beans.factory.BeanFactory
import org.springframework.context.annotation.Configuration
import java.util.*

enum class API { ZOMATO }

@Configuration
class ApiRepository(private val factory: BeanFactory) {
    private val apiMap = EnumMap<API, IApi>(API::class.java)

    fun getApi(api: API): IApi? {
        return apiMap.computeIfAbsent(api) {
            when (api) {
                API.ZOMATO -> factory.getBean(ZomatoApi::class.java)
            }
        }
    }
}