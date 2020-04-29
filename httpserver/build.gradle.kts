import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    kotlin("jvm") version "1.3.71"
    kotlin("plugin.spring") version "1.3.71"
}

group = "pt.isel.ps.g06"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
    maven {
        url = uri("https://mvnrepository.com/")
    }
    jcenter()
}

dependencies {
    //Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    //Spring
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")

    //Spring - Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

    //Mockito - Test
    testImplementation("org.mockito:mockito-all")

    //Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    //JDBI / Postgres
    val jdbiVersion = "3.12.2"
    implementation("org.jdbi", "jdbi3-core", jdbiVersion)
    implementation("org.jdbi", "jdbi3-kotlin", jdbiVersion)
    implementation("org.jdbi", "jdbi3-postgres", jdbiVersion)
    implementation("org.jdbi", "jdbi3-sqlobject", jdbiVersion)
    implementation("org.jdbi", "jdbi3-kotlin-sqlobject", jdbiVersion)
    implementation("org.jdbi", "jdbi3-spring4", jdbiVersion)
    implementation("org.postgresql", "postgresql", "42.2.12")

}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}
