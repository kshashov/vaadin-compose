import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("war")
	id("org.gretty") version "3.0.4"
	id("com.vaadin") version "0.14.6.0"
	kotlin("jvm") version "1.4.32"
}

group = "io.github.kshashov"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	jcenter()
	mavenLocal()
	mavenCentral()
}

gretty {
	contextPath = "/"
	servletContainer = "jetty9.4"
}

dependencies {
	implementation("io.github.kshashov:vaadin-compose:0.0.3-SNAPSHOT")
	implementation(enforcedPlatform("com.vaadin:vaadin-bom:14.6.0"))

	implementation("com.vaadin:vaadin-core")
	implementation("io.reactivex.rxjava3:rxjava:3.0.12")

	implementation("org.slf4j:slf4j-simple:1.7.30")
	compileOnly("javax.servlet:javax.servlet-api:3.1.0")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		useIR = true
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
