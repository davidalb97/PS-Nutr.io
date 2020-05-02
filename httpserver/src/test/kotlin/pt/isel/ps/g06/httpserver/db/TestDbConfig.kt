package pt.isel.ps.g06.httpserver.db

import org.jdbi.v3.core.Jdbi
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import pt.isel.ps.g06.httpserver.dataAccess.db.repo.MealDbRepository
import pt.isel.ps.g06.httpserver.db.repo.InsertConstants
import javax.sql.DataSource


@Configuration
class TestDatabaseConfiguration {

//    @Bean
//    fun mealRepo(jdbi: Jdbi): MealDbRepository = MealDbRepository(jdbi)

    @Bean
    fun insertConstants(jdbi: Jdbi): InsertConstants = InsertConstants(jdbi)
}