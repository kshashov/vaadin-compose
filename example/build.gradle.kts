import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.5"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("com.vaadin") version "0.14.6.0"
	kotlin("jvm") version "1.4.32"
	kotlin("plugin.spring") version "1.4.32"
}

group = "io.github.kshashov"
version = "0.0.3-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	jcenter()
//	mavenLocal()
	mavenCentral()
	maven { setUrl("https://jitpack.io") }
}

dependencyManagement {
	imports {
		mavenBom("com.vaadin:vaadin-bom:14.6.0")
	}
}

dependencies {
	implementation("com.vaadin:vaadin-spring-boot-starter")
	implementation("io.github.kshashov:vaadin-compose:0.0.6")
	implementation("io.reactivex.rxjava3:rxjava:3.0.12")

	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

vaadin { productionMode = true }

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
