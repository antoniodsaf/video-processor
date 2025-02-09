import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val mockkVersion: String by project
val kluentVersion: String by project
val awsSdkVersion: String by project
val cucumberVersion: String by project
val restAssuredVersion: String by project
val springCloudAwsVersion: String by project
val testContainersVersion: String by project
val kotlinxCoroutinesCoreVersion: String by project
val amazonSdkVersion: String by project

plugins {
    jacoco
    application

    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.sonarqube") version "4.4.1.3373"

    kotlin("jvm") version "1.9.21"
    kotlin("plugin.jpa") version "1.9.21"
    kotlin("plugin.spring") version "1.9.21"
    kotlin("plugin.allopen") version "1.9.21"
}

group = "br.com.fiap.video.processor"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("io.awspring.cloud:spring-cloud-aws-starter-sqs:$springCloudAwsVersion")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3:$springCloudAwsVersion")

    implementation("org.bytedeco:ffmpeg-platform:5.1.2-1.5.8")
    implementation("org.bytedeco:javacv-platform:1.5.8")
    implementation("org.apache.commons:commons-compress:1.23.0")


    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("com.fasterxml.jackson.core:jackson-databind")

    implementation("commons-io:commons-io:2.17.0")

    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("com.h2database:h2")

    testImplementation(platform("io.cucumber:cucumber-bom:$cucumberVersion"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("io.cucumber:cucumber-java")
    testImplementation("io.cucumber:cucumber-spring")
    testImplementation("io.cucumber:cucumber-core")

    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")

    testImplementation("io.rest-assured:rest-assured:$restAssuredVersion")

    testImplementation("org.testcontainers:localstack:$testContainersVersion")
    testImplementation("org.testcontainers:testcontainers:$testContainersVersion")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesCoreVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesCoreVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.9"
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        html.required = true
        xml.required = true
    }
}

configurations {
    val cucumberRuntime by creating {
        extendsFrom(
            configurations.testImplementation.get(),
            configurations.implementation.get(),
            configurations.runtimeOnly.get()
        )
    }
}

task("behaviorTest") {
    dependsOn("assemble", "testClasses")
    doLast {
        javaexec {
            mainClass = "io.cucumber.core.cli.Main"
            classpath = configurations["cucumberRuntime"] + sourceSets.main.get().output + sourceSets.test.get().output
            args = listOf(
                "--plugin", "pretty",
                "--glue", "classpath:br.com.fiap.video.processor.cucumber",
                "src/test/resources/cucumber/features"
            )
        }
    }
}


sonar {

    val exclusions = listOf(
        "**/VideoProcessorApp.kt",
    )

    properties {
        property("sonar.projectKey", "antoniodsaf_video-processor")
        property("sonar.organization", "antoniodsaf")
        property("sonar.junit.reportPaths", "build/test-results/test")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.gradle.skipCompile", "true")
        property("sonar.coverage.exclusions", exclusions)
    }
}