package pt.isel.ps.g06.httpserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import pt.isel.ps.g06.httpserver.db.IDbRepository

@SpringBootApplication
class HttpserverApplication {

	//Used so that we can run controllers
	//TODO!
	@Bean
	fun db() = object: IDbRepository {
		override fun getCuisines(skip: Int, count: Int): Array<String> = arrayOf("Debug cuisine!")
	}
}

fun main(args: Array<String>) {
	runApplication<HttpserverApplication>(*args)
}
