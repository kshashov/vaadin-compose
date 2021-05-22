import net.researchgate.release.GitAdapter
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    jacoco
    id("net.researchgate.release") version "2.8.1"
    id("maven-publish")
    kotlin("jvm") version "1.4.32"
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

// Use `./gradlew publish` to publish a build to Github Packages
publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/kshashov/vaadin-compose")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_PACKAGES_TOKEN")
            }
        }
    }
    publications {
        register("gpr", MavenPublication::class) {
            from(components["java"])
        }
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("io.reactivex.rxjava3:rxjava:3.0.12")
    implementation("com.vaadin:vaadin-core:14.6.0")
}

// Use `./gradlew release` to create a tagged release commit
release {
    scmAdapters = mutableListOf<Class<out net.researchgate.release.BaseScmAdapter>>(GitAdapter::class.java)
//    pushReleaseVersionBranch = "main"
    tagTemplate = "$name.$version"

    with(getProperty("git") as GitAdapter.GitConfig) {
//        requireBranch = "main"
    }
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            useIR = true
            jvmTarget = "1.8"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    afterReleaseBuild {
        dependsOn(publish)
    }

    jacocoTestReport {
        reports {
            html.isEnabled = true
            xml.isEnabled = true
        }
        dependsOn(test)
    }
}
