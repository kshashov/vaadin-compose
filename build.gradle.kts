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
	mavenCentral()
}

gretty {
	contextPath = "/"
	servletContainer = "jetty9.4"
}

extra["vaadinVersion"] = "14.5.3"

dependencies {
	implementation(enforcedPlatform("com.vaadin:vaadin-bom:14.6.0"))

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("io.reactivex.rxjava3:rxjava:3.0.12")
	implementation("com.vaadin:vaadin-core")

	compileOnly("org.projectlombok:lombok:1.18.20")
	compileOnly("javax.servlet:javax.servlet-api:3.1.0")

	annotationProcessor("org.projectlombok:lombok")

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
