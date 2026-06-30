plugins {
	id("org.springframework.boot") version "3.4.2"
	id("io.spring.dependency-management") version "1.1.7"
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.9.25"
}

repositories {
	mavenCentral()
}
dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
	testImplementation("org.mockito:mockito-junit-jupiter:5.12.0")
}


tasks.withType<Test> {
	useJUnitPlatform()
}