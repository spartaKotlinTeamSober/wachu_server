plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    kotlin("plugin.jpa") version "1.9.24"
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
    kotlin("kapt") version "1.9.24"
}

group = "sparta.nbcamp"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

val springAiVersion = "1.0.0-M1"
val kotestVersion = "5.8.1"
val queryDslVersion = "5.0.0"
val mockkVersion = "1.13.8"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("javax.servlet:javax.servlet-api:4.0.1")

    // OpenAI
    implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter:${springAiVersion}")

    // Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // JJWT
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.security:spring-security-test")

    // MockK
    testImplementation("io.mockk:mockk:$mockkVersion")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:${kotestVersion}")
    testImplementation("io.kotest:kotest-assertions-core:${kotestVersion}")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")

    // QUERY DSL
    implementation("com.querydsl:querydsl-jpa:$queryDslVersion:jakarta")
    kapt("com.querydsl:querydsl-apt:$queryDslVersion:jakarta")

    //aws
    implementation("software.amazon.awssdk:s3:2.20.0")
    implementation("org.springframework.cloud:spring-cloud-starter-aws:2.0.1.RELEASE")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    //Apache Tika
    implementation("org.apache.tika:tika-core:2.4.1")
    implementation("org.apache.tika:tika-parsers-standard-package:2.4.1")

    //EmailValidation
    implementation("org.springframework.boot:spring-boot-starter-mail")

    //h2
    testRuntimeOnly("com.h2database:h2")
    //mySql
    implementation("mysql:mysql-connector-java:8.0.30")

    // test
    testImplementation("com.h2database:h2")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
