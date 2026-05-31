plugins {
	java
	id("org.springframework.boot") version "3.5.14"
	id("io.spring.dependency-management") version "1.1.7"
	id("com.github.spotbugs") version "6.5.5"
}

group = "tobyspring"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val mockitoAgent = configurations.create("mockitoAgent")

dependencies {
	implementation("org.springframework.security:spring-security-core")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")

	compileOnly("org.projectlombok:lombok")

	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	runtimeOnly("com.h2database:h2")
	runtimeOnly("com.mysql:mysql-connector-j")

	annotationProcessor("org.projectlombok:lombok")

	testCompileOnly("org.projectlombok:lombok")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	testAnnotationProcessor("org.projectlombok:lombok")

	testImplementation("org.mockito:mockito-core")
	testImplementation("org.junit-pioneer:junit-pioneer:2.3.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("com.tngtech.archunit:archunit-junit5:1.4.1")

	mockitoAgent("org.mockito:mockito-core") { isTransitive = false }
}

tasks.withType<Test> {
	useJUnitPlatform()
	jvmArgs("-javaagent:${mockitoAgent.asPath}")
}
