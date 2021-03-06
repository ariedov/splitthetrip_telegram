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
    compile(kotlin("stdlib"))
    compile("io.github.seik.kotlin-telegram-bot", "telegram", "0.3.7")
    compile("org.litote.kmongo", "kmongo", "3.10.1")

    testImplementation("junit", "junit", "4.12")
    testImplementation("com.nhaarman.mockitokotlin2", "mockito-kotlin", "2.1.0")
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

tasks.withType<Jar> {
    manifest {
        attributes(mapOf("Main-Class" to application.mainClassName))
    }
    val version = "1.0-SNAPSHOT"

    archiveName = "${application.applicationName}-$version.jar"
    from(configurations.compile.map { if (it.isDirectory) it else zipTree(it) })
}
