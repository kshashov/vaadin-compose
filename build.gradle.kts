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
    tagCommitMessage = "Creating tag:"
    newVersionCommitMessage = "New version commit:"
    preTagCommitMessage = "Pre tag commit:"
    tagTemplate = "\$version"

    with(getProperty("git") as GitAdapter.GitConfig) {
        requireBranch = "feature/1"
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

    updateVersion {
        doFirst {
            val file = file("README.md")
            var content = file.readText()
            val versionPattern = "\\d+(?:\\.\\d+)+"
            content = content.replace(
                Regex("<version>${versionPattern}</version>"),
                "<version>${version}</version>"
            )
            content = content.replace(
                Regex("implementation 'com.github.kshashov:vaadin-compose:${versionPattern}'"),
                "implementation 'com.github.kshashov:vaadin-compose:${version}'"
            )
            file.writeText(content)
        }
    }
}
