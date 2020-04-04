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
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }

//    implementation("org.jdbi", "jdbi3-core")
//    implementation("org.jdbi", "jdbi3-kotlin")
//    implementation("org.jdbi", "jdbi3-kotlin-sqlobject")
//    implementation("org.jdbi", "jdbi3-postgres")

    implementation("org.postgresql", "postgresql", "42.2.5")
    implementation("org.jdbi", "jdbi3-core", "3.5.1")
    implementation("org.jdbi", "jdbi3-kotlin", "3.5.1")
    implementation("org.jdbi", "jdbi3-kotlin-sqlobject", "3.5.1")
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
