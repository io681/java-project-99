import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
	application
//	java
	id("checkstyle")
	id("io.freefair.lombok") version "8.4"
	jacoco
	id("org.springframework.boot") version "3.2.4"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "hexlet.code"
version = "0.0.1-SNAPSHOT"

repositories {
	mavenCentral()
}

application { mainClass.set("hexlet.code.app.Application") }

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
//	implementation("org.springframework.boot:spring-boot-starter-actuator")
//	implementation("org.springframework.boot:spring-boot-configuration-processor")
//	implementation("org.springframework.boot:spring-boot-starter-security")
//	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	runtimeOnly("com.h2database:h2:2.2.224")
	implementation("org.postgresql:postgresql:42.6.0")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("org.mapstruct:mapstruct:1.6.0.Beta1")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.0.Beta1")

	implementation("org.instancio:instancio-junit:3.6.0")
	implementation("net.javacrumbs.json-unit:json-unit-assertj:3.2.2")
	implementation("net.datafaker:datafaker:2.0.2")

	runtimeOnly("com.h2database:h2:2.2.224")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation("org.springframework.security:spring-security-test")
	testImplementation(platform("org.junit:junit-bom:5.10.1"))
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
}

tasks.test {
	useJUnitPlatform()
	// https://technology.lastminute.com/junit5-kotlin-and-gradle-dsl/
	testLogging {
		exceptionFormat = TestExceptionFormat.FULL
		events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
		// showStackTraces = true
		// showCauses = true
		showStandardStreams = true
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
