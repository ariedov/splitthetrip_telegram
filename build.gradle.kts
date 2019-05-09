plugins {
    application
    jacoco
    kotlin("jvm") version "1.3.30"
}

jacoco {
    toolVersion = "0.8.2"
}

application {
    mainClassName = "com.dleibovych.splitthetrip.MainKt"
    group = "com.dleibovych"
    version = "1.0-SNAPSHOT"
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.github.seik.kotlin-telegram-bot", "telegram", "0.3.7")

    testImplementation("junit", "junit", "4.12")
}

repositories {
    jcenter()
    maven { setUrl("https://jitpack.io") }
}

tasks.getByName<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("test"))

    reports {
        xml.isEnabled = true

        html.isEnabled = true

        csv.isEnabled = false
    }
}
